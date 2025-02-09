(async function () {
    const modalDelete = document.getElementById("modal-delete");
    const modalEdit = document.getElementById("modal-edit");
    modalDelete.addEventListener('show.bs.modal', event => {
        alert("hello");
        // let button = event.relatedTarget; // Button that triggered the modal
        // // Extract info from data-* attributes
        // modalDelete.querySelector("#deleteId").value = button.data('uid');
    })

    const updateTable = async () => {
        const getRow = user => {
            let out = '<tr>';
            Object.keys(user).forEach(k => {
                if (k === "password") return;
                out += `<td data-name=${k}>${k === "roles" ? user[k].reduce((a, b) => a + ` ${b}`, "") : user[k]}</td>`;
            });
            out += ` <td>
                        <button type="button" class="btn btn-info px-2 py-1 "
                             data-uid=${user.id}>Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger px-1 py-1"
                             data-uid=${user.id}>Delete</button>
                    </td>
                </tr>`;
            return out;
        };
        try {
            const table = document.getElementById("usersTable").querySelector("tbody");
            const resp = await fetch("/api/admin/users");

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
    document.getElementById("usersTable").addEventListener("click", evt => {

        if (evt.target.classList.contains("btn-info")) {

            // showModal(modal, true);
            // fillInputs(evt.target.parentElement.parentElement);
            // convertModal(false);
        }

        if (evt.target.classList.contains("btn-danger")) {
            modalDelete.querySelector('#deleteId').value = evt.target.dataset.uid;
            $('#modal-delete').modal('show');
            // showModal(modal, true);
            // fillInputs(evt.target.parentElement.parentElement);
            // convertModal(true);
        }
    });
    await updateTable();
})()