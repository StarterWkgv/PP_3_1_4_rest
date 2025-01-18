(function () {

    let modal = document.querySelector(".modal-container");
    let showModal = (m, show) => {
        if (show) {
            m.style.display = "block";
            return;
        }
        m.style.display = "none";
    };
    document.getElementById("info").addEventListener("click", e => {
        if (e.target.classList.contains("button-edit")) {
            showModal(modal, true);
        }

        if (e.target.classList.contains("button-delete")) {

        }

    })
})();