<#import "header.ftl" as macros>
<@macros.header title="Login Page" css=["https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css","https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css","https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/css/intlTelInput.css"]/>

<body>
<div class="h-screen w-screen bg-white flex justify-center items-center font-poppins">
    <form class="w-4/5 sm:w-1/2 sm:block lg:w-1/3 grid rounded-lg bg-white shadow-md border border-slate-100 py-10 space-y-5" id="loginForm">
        <h1 class="text-center m-0 mb-3 text-xl sm:text-xl md:text-2xl text-gray-700 font-bold font-poppins cursor-default">
            Welcome Back
        </h1>
        <div class="w-4/5 mb-2 m-auto flex flex-col">
            <label for="mobileNumber" class="text-sm mb-1 text-gray-500">Mobile Number</label>
            <input maxlength="15" name="mobileNumber" type="tel" id="mobileNumber" class="l-6 outline-0 h-10 w-full border border-slate-300 rounded-md text-base pl-10 font-poppins border-box focus:outline-none" required/>
            <div class="error-message text-xs text-red-600 mt-1"></div>
        </div>
        <div class="w-4/5 mb-2 m-auto relative">
            <label for="password" class="text-sm text-gray-500 mb-1">Password</label>
            <input name="password" placeholder="Enter Password" type="password"
                   class="l-6 outline-0 h-10 w-full border border-slate-300 rounded-md text-base pl-2 font-poppins border-box focus:outline-none"
                   required>
            <i id="togglePassword" class="fas fa-eye-slash absolute right-3 top-9 cursor-pointer text-gray-300 text-sm"></i>
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

      passwordInput.addEventListener("input",()=>{
      clearErrorMessages();
      })

      mobileNumberInput.addEventListener("input", function (event) {
        clearErrorMessages();
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
          },{
          header:{
          authorization:"Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzbkJpZWxZOFBFUzlPQW5JUXl6MG1jcXh5S3dseEhkZnhNX0pZTHJYRjFZIn0.eyJleHAiOjE3MTQ0ODE2MjgsImlhdCI6MTcxNDQ4MTMyOCwiYXV0aF90aW1lIjoxNzE0NDgxMzI3LCJqdGkiOiJkZWUwOTZiNS1hYjI5LTRjNTEtOTdjNy1mNDdlNjY0MGYwMDciLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvcmVhbG1zL2Vhc3lfdHJhbnNmZXJfcmVhbG0iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOWI3YjFjMmYtNjIxMi00ZDEyLTkxODItOGE4OTM3YjEzMGUxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicmVhY3QiLCJub25jZSI6ImQ1YWMxMTcxLWNiNWYtNDZlMC1hNzNjLTkyYzMzYTI2MTg2MiIsInNlc3Npb25fc3RhdGUiOiIyNDMxNTg3OS00ZWJmLTQ4MDUtYmU1OS1lOTAxNGJjNjk5ZmIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6MzAwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1lYXN5X3RyYW5zZmVyX3JlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiIyNDMxNTg3OS00ZWJmLTQ4MDUtYmU1OS1lOTAxNGJjNjk5ZmIiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlJhamVzaGthbnRoIE0iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0IiwiZ2l2ZW5fbmFtZSI6IlJhamVzaGthbnRoIiwiZmFtaWx5X25hbWUiOiJNIiwiZW1haWwiOiJ0ZXN0QGdtYWlsLmNvbSJ9.guggQ3a0ZhBBGKU7YTcY3UOFK6fRJMWXabN7KB359rPMPO93xfGB2R_TxgwvK0kiDgmyhf55KuJsAGEMXyKV03QUyiIM30XdmRrSOyzG7AnC_vADr64YR9Hvs3ctBviDRdvSX-8kGQJ_DHmTP8thzEf8vPBPMfuvuz6SGj6PBv3jvYzrYQ1McCxFSBqTFID5XRuX7LLRNLI70XBPOT2hlddadebJX6q7JS_tcjIEOYJelLtKBx0csff3eNTsuSSTYqfNIIvU7JyxRWWBC6-a9MTokq7GDuBAYciGMo_S79H6d6P0uCDxQML9QCt4wIgTG3fm4hK4wCCUW9UEnCd_Jg"}})
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
                displayErrorMessage(passwordError, "Invalid credentials");
                mobileNumberInput.value = "";
                passwordInput.value = "";
                break;
              case 202:
                displayErrorMessage(passwordError, "Invalid credentials");
                mobileNumberInput.value = "";
                passwordInput.value = "";
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
