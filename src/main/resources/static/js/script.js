window.addEventListener("keydown", e => {
    let podgonka = document.querySelector(".podgonka");
    if (e.ctrlKey && e.keyCode === 81) {
        e.preventDefault();
        if (podgonka.style.display === "none") {
            podgonka.style.display = "block";
        } else {
            podgonka.style.display = "none";
        }
    }
    if (!podgonka.hidden && e.keyCode === 38) {
        e.preventDefault();
        let op = parseFloat(getComputedStyle(podgonka).opacity);
        if (op < 1) {
            podgonka.style.opacity = Math.min(op + 0.1, 1);
        }
    }
    if (!podgonka.hidden && e.keyCode === 40) {
        e.preventDefault();
        let op = parseFloat(getComputedStyle(podgonka).opacity);
        if (op >= 0) {
            podgonka.style.opacity = Math.max(op - 0.1, 0);
        }
    }
});