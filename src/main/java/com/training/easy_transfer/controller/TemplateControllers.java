package com.training.easy_transfer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.easy_transfer.model.Action;
import com.training.easy_transfer.model.Transactions;
import com.training.easy_transfer.service.TransactionsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TemplateControllers {
    private final TransactionsService transactionsService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private static final String ERROR_FTL = "error";
    private final RestTemplate restTemplate;

    @Value("${keycloak.introspect-url}")
    private String introspectUrl;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value(("${spring.security.oauth2.client.registration.keycloak.client-id}"))
    private String clientId;


    @Autowired
    public TemplateControllers(RestTemplate restTemplate, TransactionsService transactionsService, OAuth2AuthorizedClientService authorizedClientService) {
        this.restTemplate = restTemplate;
        this.transactionsService = transactionsService;
        this.authorizedClientService = authorizedClientService;

    }

    @GetMapping("/")
    public ModelAndView myPage(HttpServletRequest request, HttpServletResponse response, OAuth2AuthenticationToken authenticationToken) throws ServletException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authorizedClientService != null) {

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(),
                    authentication.getName()
            );

            if (client == null) {
                request.logout();
                modelAndView.setViewName(ERROR_FTL);
            }

            try {
                assert client != null;
                String accessToken = client.getAccessToken().getTokenValue();
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                String mobileNumber = oauth2User.getAttribute("preferred_username");

                if (mobileNumber != null) {
                    List<Transactions> transactions = transactionsService.getTransactionsByMobileNumber(mobileNumber);
                    List<Transactions> pendingTransactions = transactions.stream()
                            .filter(transaction -> "pending".equals(transaction.getStatus()))
                            .collect(Collectors.toList());
                    modelAndView.addObject("transactions", pendingTransactions);
                    modelAndView.addObject("mobileNumber", mobileNumber);
                    modelAndView.addObject("accessToken", accessToken);
                } else {
                    modelAndView.addObject("hasMobileNumber", "Mobile number is invalid or not present.");
                }
            } catch (Exception e) {
                modelAndView.setViewName(ERROR_FTL);
            }
        } else {
            modelAndView.setViewName(ERROR_FTL);
        }
        return modelAndView;
    }


    @PostMapping("/api/transaction/pending/{mobileNumber}")
    public ResponseEntity<String> newAlert(@PathVariable String mobileNumber, JwtAuthenticationToken authenticationToken, HttpServletRequest request) throws JsonProcessingException, ServletException {

        String accessToken = authenticationToken.getToken().getTokenValue();

        ResponseEntity<String> introspectionResponse = introspectToken(accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseBodyMap = objectMapper.readValue(introspectionResponse.getBody(), new TypeReference<Map<String, Object>>() {
        });

        Boolean isActive = (Boolean) responseBodyMap.get("active");

        if (isActive == null || !isActive) {
            request.logout();
            SecurityContextHolder.clearContext();

        }

        List<Transactions> pendingTransactions = transactionsService.checkForPendingTransactions(mobileNumber);
        if (!pendingTransactions.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Pending transactions found");
        } else {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("No pending transactions found");
        }
    }

    @GetMapping("/success")
    public ModelAndView successPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @GetMapping("/canceled")
    public ModelAndView canceledPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("canceled");
        return modelAndView;
    }

    @PostMapping("/api/transaction/confirm")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> confirmPayment(@RequestBody Action request) {
        String action = request.getStatus();
        Long id = request.getId();
        Optional<Transactions> optionalTransaction = transactionsService.findById(id);

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Transactions transaction = optionalTransaction.get();

        if (action.equalsIgnoreCase("confirm")) {
            transaction.setStatus("confirmed");
            transactionsService.saveTransaction(transaction);
            return ResponseEntity.ok("Payment confirmed successfully");
        } else if (action.equalsIgnoreCase("cancel")) {
            transaction.setStatus("canceled");
            transactionsService.saveTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.badRequest().body("Invalid request");
    }

    private ResponseEntity<String> introspectToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("token", accessToken);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(
                introspectUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String logoutToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            // Invalidate session for OAuth2 user
            SecurityContextHolder.clearContext();
            return "Logout successful";
        }
        return "Logout not supported for this user";
    }

}