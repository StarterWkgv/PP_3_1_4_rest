(function (){
document.getElementById("info").addEventListener("click", e =>{
    if (e.target.classList.contains("button-edit")){
        alert("buttonn click");
    }

    if (e.target.classList.contains("button-delete")){
    alert("delete")    ;
    }

})
})();