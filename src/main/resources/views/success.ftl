<#import "header.ftl" as macros />
<@macros.header title="Payment Success" css=["https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"] includeCss="css/style.css"/>

<body>
  <div class="font-poppins m-0 bg-green-300 h-screen w-screen flex flex-col items-center justify-center">
    <div class="wrapper h-36 w-screen flex justify-center items-center bg-green-300">
      <svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
        <circle class="checkmark__circle" cx="26" cy="26" r="25" fill="none"/>
        <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
      </svg>
    </div>
    <h3 class="mb-3">payment transaction successful!</h3>
    <p>redirecting to the home page...</p>
  </div>

  <script>

    setTimeout(() => {
        window.location.href = "/";
    }, 3000);

  </script>
</body>
</html>
