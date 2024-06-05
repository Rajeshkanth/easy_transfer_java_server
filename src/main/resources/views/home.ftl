<#import "header.ftl" as macros>
<@macros.header title="Transaction Alerts" css=["https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css", "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/css/intlTelInput.css"]/>

<body>
<div class="header z-50  p-3 justify-evenly font-poppins font-bold flex items-center text-left w-screen bg-gray-700 fixed top-0 text-black">
  <h1 class="m-0  w-3/4  text-white">Transaction Alerts</h1>
  <button class="border outline-none font-bold w-20 hover:text-white hover:cursor-pointer bg-white text-gray-800 hover:bg-gray-700 border-gray-800 p-1 rounded-md" id="logout">Log out</button>
</div>
<#if transactions??>
<#if transactions?size !=0>
  <#list transactions as alert>

  <div class="form-container mt-12 font-poppins border shadow-md w-full sm:w-4/5 md:w-1/2 m-auto p-4 mb-4 rounded-md" id="${alert.tabId}">
    <div class="transfer-details-container p-2 border-b border-gray-200 pl-2">
      <h5 class="transfer-text m-0 mt-2 mb-1">Transfer Details</h5>
      <div class=" flex justify-between items-center sm:pr-4">
        <h5 class="m-1 mt-1 text-gray-600">Recipient</h5>
        <h4 class="m-1 mt-1 font-bold">${alert.name}</h4>
      </div>
      <div class=" flex justify-between items-center sm:pr-4">
        <h5 class="m-1 mt-1 text-gray-600">Account number</h5>
        <h4 class="m-1 mt-1 font-bold">${alert.accountNumber}</h4>
      </div>

      <div class="flex justify-between items-center sm:pr-4">
        <h5 class="m-1 mt-1 text-gray-600">IFSC code</h5>
        <h4 class="m-1 mt-1 font-bold">${alert.ifsc}</h4>
      </div>
      <div class="flex justify-between text-center items-center sm:pr-4">
        <h5 class="m-1 mt-1 text-gray-600">Sending amount</h5>
        <h1 class="m-1 mt-1 font-bold text-xl">${alert.amount}</h1>
      </div>
    </div>

    <div class="btn-container flex justify-around text-white pt-4">
      <button class="button confirm bg-green-600 hover:bg-green-500 w-2/5 border border-green-300 inline-flex items-center appearance-none rounded-md border-none shadow-md cursor-pointer font-bold text-sm h-10 justify-center tracking-tighter leading-normal max-w-full overflow-visible px-5 py-1 relative text-center transition duration-300 ease-in-out select-none w-45 mt-3 mb-4" onclick="confirm('${alert.tabId}', '${alert.mobileNumber}','${alert.id}')">
        Confirm
      </button>
      <button class="button cancel bg-red-500 hover:bg-red-400 w-2/5 border border-red-300 inline-flex items-center appearance-none rounded-md border-none shadow-md cursor-pointer font-bold text-sm h-10 justify-center tracking-tighter leading-normal max-w-full overflow-visible px-5 py-1 relative text-center transition duration-300 ease-in-out select-none w-45 mt-3 mb-4" onclick="cancel('${alert.tabId}','${alert.mobileNumber}','${alert.id}')">
        Cancel
      </button>
    </div>
   </div>
  </#list>
  <#else>
  <div class="w-full h-full flex justify-center items-center mt-12 pt-4">
  <h1 class="text-gray-800 text-base mt-4">No payment requests found</h1>
  </div>
  </#if>
  <#else>
    <div class="w-full h-full flex justify-center items-center mt-12 pt-4">
      <h1 class="text-gray-800 text-base mt-4">Invalid session.</h1>
    </div>
  </#if>


<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>

    const logoutBtn = document.querySelector("#logout");

    logoutBtn.addEventListener("click", () => {
         window.location.href = "/logout";
    });

    const sendAction =async (status, tabId, mobileNumber, id)=>{

        try {
            const response = await axios.post("http://localhost:8080/api/transaction/confirm",
                {
                  status: status,
                  tabId:tabId,
                  id:id,
                  mobileNumber:mobileNumber
                },{
                  headers:{
                     Authorization:`Bearer ${accessToken}`},
                });

                if(response.status===200){
                    window.location.href="/success";
                }else if(response.status===201){
                     window.location.href="/canceled";
                }else{
                    alert("server error!");
                }
        }catch(err){
            console.log(err)
        }
    }

    const confirm = ( tabId, mobileNumber, id) => {
        sendAction("confirm", tabId, mobileNumber, id);
    };

    const cancel = ( tabId, mobileNumber, id) => {
        sendAction("cancel", tabId, mobileNumber, id);
    };

   setInterval(async () => {
      const checkForNewAlert = async () => {
            try {
                const response = await axios.post(`http://localhost:8080/api/transaction/pending/${mobileNumber}`,
                {},{
                headers:{
                Authorization:`Bearer ${accessToken}`},
                });

                if (response.status === 200) {
                      window.location.reload();
                }
            } catch (err) {
                  console.log(err);
            }
          };

      checkForNewAlert();
   }, 5000);

</script>

</body>
</html>