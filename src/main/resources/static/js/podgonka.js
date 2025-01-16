//TODO: remove
// UP and DOWN arrows: +/- opacity
// Ctrl+q: show/hide img for podgonka

window.addEventListener("keydown", e => {
    let podgonka = document.querySelector(".podgonka");
    let caption = document.querySelector(".podgonka-caption");
    if (e.ctrlKey && e.keyCode === 81) {
        e.preventDefault();
        caption.textContent = "image opacity: " + getComputedStyle(podgonka).opacity;
        if (getComputedStyle(podgonka).display=== "none") {
            podgonka.style.display = "block";
        } else {
            podgonka.style.display = "none";
        }
    }
    if (!podgonka.hidden && e.keyCode === 38) {
        e.preventDefault();

        caption.textContent = "image opacity: " + getComputedStyle(podgonka).opacity;
        let op = parseFloat(getComputedStyle(podgonka).opacity);
        if (op < 1) {
            podgonka.style.opacity = Math.min(op + 0.1, 1) + '';
        }
    }
    if (!podgonka.hidden && e.keyCode === 40) {
        e.preventDefault();
        caption.textContent = "image opacity: " + getComputedStyle(podgonka).opacity;
        let op = parseFloat(getComputedStyle(podgonka).opacity);
        if (op >= 0) {
            podgonka.style.opacity = Math.max(op - 0.1, 0) + '';
        }
    }
});