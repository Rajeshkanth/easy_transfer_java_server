<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <#include "css/canceled.css">
</head>
<body>
    <div
      class="font-poppins bg-green-200 h-screen w-screen flex flex-col items-center justify-center"
    >
      <div
        class="h-36 w-screen flex justify-center items-center bg-green-200"
      >
        <svg class="crossmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
          <circle class="crossmark__circle" cx="26" cy="26" r="25" fill="none" />
          <path class="crossmark__check" fill="none" d="M16 16 36 36 M36 16 16 36" />
        </svg>
      </div>
      <h3 class="mb-2">payment transaction failed!</h3>
      <p>redirecting to the home page...</p>
    </div>

    <script>
      const mobileNumber = "${mobileNumber}";

      setTimeout(() => {
        window.location.href = "/home/"+mobileNumber;
      }, 3000);
    </script>
  </body>
</html>
