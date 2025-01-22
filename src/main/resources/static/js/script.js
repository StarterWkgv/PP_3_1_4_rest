const modal = document.querySelector(".modal-container");
const csrfValue = document.querySelector("[name=_csrf]").value;
const csrfHeader = "X-CSRF-TOKEN";
const modalFields = new Map();

document.getElementById("modal-fields")
    .querySelectorAll("input, select").forEach(node => {
    modalFields.set(node.id, node);
});

const showModal = (m, show) => {
    if (!show) {
        m.style.display = "none";
        return;
    }
    m.style.display = "block";
};

const disableFields = disable => {
    if (disable) {
        modalFields.forEach((v, k) => {
            if (k === "password") {
                v.style.display = "none";
                v.previousElementSibling.style.display = "none";
                return;
            }
            v.disabled = true;
        });
        return;
    }
    modalFields.forEach((v, k) => {
        if (k === "id") return;
        if (k === "password") {
            v.style.display = "block";
            v.previousElementSibling.style.display = "block";
            return;
        }
        v.disabled = false;
    })
}

const fillInputs = (row) => {
    if (!row) return;
    row.querySelectorAll('[data-name]').forEach(n => {
        let modalField = modalFields.get(n.dataset.name);
        if (n.dataset.name === "role") {
            Array.from(modalField.options).forEach(opt => {
                opt.selected = n.textContent.includes(opt.value);
            })
        } else {
            modalField.value = n.textContent;
        }
    })
    // selectedOptions
}

//edit / delete event handler
document.getElementById("info").addEventListener("click", evt => {

    //edit handler
    if (evt.target.classList.contains("button-edit")) {
        showModal(modal, true);
        fillInputs(evt.target.parentElement.parentElement);
        disableFields(false);
    }

    //delete handler
    if (evt.target.classList.contains("button-delete")) {
        showModal(modal, true);
        fillInputs(evt.target.parentElement.parentElement);
        disableFields(true);

    }

});

//hide modal by click
modal.addEventListener("click", evt => {
    if (evt.target.classList.contains("modal-container")) {
        showModal(modal, false, null);

    }
});

document.getElementById("modal-cross")
    .addEventListener("click", e => showModal(modal, false));

document.getElementById("modal-close")
    .addEventListener("click", e => showModal(modal, false));

document.getElementById("modal-delete")
    .addEventListener("click", e => {
        let id = modalFields.get("id");
        const reqHeaders = new Headers();
        reqHeaders.append(csrfHeader,csrfValue);
        reqHeaders.append('Content-Type', 'application/json',)
        fetch(`/admin/users?id=${id.value}`, {
            method: 'DELETE',
            headers: reqHeaders,
        })
            .then(resp => {
                if(resp.ok){
                    console.log(resp.statusText);
                    // updateTable();
                } else {
                    console.error(resp.statusText);

                }
            })
            .catch(er => console.error("Ashibka: ", er));
    });
// })();