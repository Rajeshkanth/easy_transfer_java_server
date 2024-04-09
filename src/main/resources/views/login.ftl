<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/css/intlTelInput.css" rel="stylesheet">
</head>
<body>

<div class="h-screen w-screen bg-gray-800 flex justify-center items-center font-poppins">
    <form class="w-4/5 sm:w-1/2 sm:block lg:w-1/3 grid rounded-lg bg-white shadow-md py-10 space-y-5" id="loginForm">
        <h1 class="text-center m-0 mb-3 text-xl sm:text-xl md:text-2xl text-gray-700 font-bold font-poppins cursor-default">
            Welcome Back
        </h1>
        <div class="w-4/5 mb-2 m-auto flex flex-col">
            <label for="mobileNumber" class="text-sm mb-1">Mobile Number</label>
            <input maxlength="15" name="mobileNumber" type="tel" id="mobileNumber" class="l-6 outline-0 h-10 w-full border border-slate-300 rounded-md text-base pl-10 font-poppins border-box focus:outline-none"/>
            <div class="error-message text-xs text-red-600 mt-1"></div>
        </div>
        <div class="w-4/5 mb-2 m-auto relative">
            <label for="password" class="text-sm mb-1">Password</label>
            <input name="password" placeholder="Enter Password" type="password"
                   class="l-6 outline-0 h-10 w-full border border-slate-300 rounded-md text-base pl-2 font-poppins border-box focus:outline-none"
                   required>
            <i id="togglePassword" class="fas fa-eye-slash absolute right-3 top-9 cursor-pointer text-gray-400"></i>
            <div class="password-error-message text-xs text-red-600 mt-1"></div>
        </div>
        <div class="w-4/5 mb-4 m-auto">
            <input type="submit" value="Login"
                   class="w-full mt-3 mb-4 border-0 outline-0 hover:bg-gray-600 bg-gray-800 text-white text-center cursor-pointer p-2 font-bold h-auto rounded-md">
        </div>
    </form>
    
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/intlTelInput.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>
      const form = document.querySelector("form");
      const mobileNumberInput = document.querySelector("#mobileNumber");
      const passwordInput = document.querySelector("input[name='password']");
      const mobileNumberError = document.querySelector(".error-message");
      const passwordError = document.querySelector(".password-error-message");
      const toggleEye = document.querySelector("#togglePassword");

      toggleEye.addEventListener("click",(event)=>{
          const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
          passwordInput.setAttribute("type", type);
          toggleEye.classList.toggle("fa-eye-slash");
          toggleEye.classList.toggle("fa-eye");
      });

      const iti = window.intlTelInput(mobileNumberInput, {
        fixDropDownWidth: true,
        initialCountry: "in",
        utilsScript:
          "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/utils.js",
      });

      mobileNumberInput.addEventListener("input", function (event) {
        const dialCode = event.target.value.replace(/\D/g, "");
        const countryCode = iti
          .getCountryData()
          .find((country) => country.dialCode === "+" + dialCode)?.iso2;
        if (countryCode) {
          iti.setCountry(countryCode);
          const phoneNumberWithoutDialCode = event.target.value.replace(
            "+" + dialCode,
            ""
          );
          event.target.value = phoneNumberWithoutDialCode;
        }
      });

      const handlePollingLogin = () => {
        const mobileNumber = mobileNumberInput.value;
        axios
          .post("/api/auth/login", {
            mobileNumber: mobileNumber,
            password: passwordInput.value,
          })
          .then((res) => {
            switch (res.status) {
              case 200:
                const url = "/home/"+mobileNumber;
                window.location.href =url;
                clearErrorMessages();
                mobileNumberInput.value = "";
                passwordInput.value = "";
                break;
              case 201:
                displayErrorMessage(mobileNumberError, "Invalid credentials");
                break;
              case 202:
                displayErrorMessage(passwordError, "Invalid credentials");
                break;
            }
          })
          .catch((err) => {
            return err;
          });
      };

      const clearErrorMessages = () => {
        mobileNumberError.innerText = "";
        passwordError.innerText = "";
      };

      const displayErrorMessage = (element, message) => {
        element.innerText = message;
      };

      const login = (event) => {
        event.preventDefault();
        const mobileNumber = mobileNumberInput.value;
        const password = passwordInput.value;
        const isValidNumber = iti.isValidNumber(mobileNumber);

        if (!mobileNumber || !password || !isValidNumber) {
           mobileNumberError.innerText = "";
           passwordError.innerText = "";

          if (!mobileNumber || !isValidNumber) {
              displayErrorMessage(mobileNumberError, mobileNumber ? "Invalid number" : "Enter mobile number");
            }
          if (!password) {
              displayErrorMessage(passwordError, "Enter password");
            }
            return;
        }

        const connectionType = "<%= connectionType %>";
        connectionType === "socket" ? handleSocketLogin() : handlePollingLogin();
      };

      form.addEventListener("submit", login);
    </script>
</body>
</html>
