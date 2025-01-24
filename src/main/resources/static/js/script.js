// (function(){
const modal = document.querySelector(".modal-container");
const csrfValue = document.querySelector("[name=_csrf]").value;
const csrfHeader = "X-CSRF-TOKEN";
const modalFields = new Map();
const errorFields = new Map();
let errorObj = null;

modal.querySelectorAll("[data-name]").forEach(node => errorFields.set(node.dataset.name, node));

document.getElementById("modal-fields")
    .querySelectorAll("input, select").forEach(node => modalFields.set(node.id, node));

const clearErrorFields = (ef) => ef.forEach((v, k) => {
    v.style.display = "none";
    v.textContent = "";
    errorObj = null;
});

const showModal = (m, show) => {
    if (!show) {
        m.style.display = "none";
        return;
    }
    clearErrorFields(errorFields)
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
        if (n.dataset.name === "roles") {
            Array.from(modalField.options).forEach(opt => {
                opt.selected = n.textContent.includes(opt.value);
            })
        } else {
            modalField.value = n.textContent;
        }
    })
    // selectedOptions
}

const updateTable = async () => {
    const getRow = user => {
        let out = '<tr>';
        Object.keys(user).forEach(k => {
            if (k === "password") return;
            out += `<td data-name=${k}>${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]}</td>`;
        });
        out += `<td>
                <button class="button button-edit">Edit</button>
                <td>
                <button class="button button-delete">Delete</button>
                </td>
                </tr>`;
        return out;
    };
    try {
        const table = document.querySelector("tbody");
        const resp = await fetch("/api/users");

        if (resp.ok) {
            let users = await resp.json();
            table.innerHTML = users.map(getRow).reduce((a, b) => a + b, "");
        } else {
            console.error(resp.status, " Couldn't get the list of users")
        }

    } catch (e) {
        console.error("Error >>> ", e);
    }
};
const fetchAndUpdate = (met, readData) => {
    return async () => {
        try {
            clearErrorFields(errorFields);
            let id = modalFields.get("id");
            const reqHeaders = new Headers();
            reqHeaders.append(csrfHeader, csrfValue);
            reqHeaders.append('Content-Type', 'application/json',);
            console.log(readData ? readData() : '');
            const response = await fetch(`/api/users/${id.value}`,
                {
                    method: met,
                    headers: reqHeaders,
                    body: readData ? readData() : ''
                });

            if (response.ok) {
                console.log(response.status);
                try {
                    clearErrorFields(errorFields);
                    await updateTable();
                } catch (e) {
                    console.log("Couldn't update the table");
                }
            } else if (response.status === 400) {
                errorObj = await response.json();
                if (errorObj) {
                    console.log(errorObj);
                    for (let i in errorObj) {
                        errorFields.get(i).textContent = errorObj[i];
                        errorFields.get(i).style.display = "block";
                    }
                } else {
                    console.error(response.status, ` couldn't perform ${met} operation`);
                }
            } else {
                console.error(response.status, ` couldn't perform ${met} operation`);
            }
        } catch (e) {
            console.error("Error >>> ", e);
        } finally {
            if (!errorObj) showModal(modal, false);
        }
    }
};

const readFields = () => {
    let user = {};
    modalFields.forEach((v, k) => {
        user[k] = k === "roles" ? Array.from(v.selectedOptions).map(r => r.value) : v.value;
    });
    return JSON.stringify(user);
};
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
    if (evt.target.classList.contains("modal-container") || (evt.target.id === "modal-cross") || (evt.target.id === "modal-close")) {
        showModal(modal, false);
    }
});

// document.getElementById("modal-cross").addEventListener("click", () => showModal(modal, false));
//
// document.getElementById("modal-close").addEventListener("click", () => showModal(modal, false));

document.getElementById("modal-delete").addEventListener("click", fetchAndUpdate("DELETE"));

document.getElementById("modal-edit").addEventListener("click", fetchAndUpdate("PUT", readFields));


// })();