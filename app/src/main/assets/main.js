function debug(value) {
	document.getElementById("debug").innerText = value;
}

class AppShortcut extends HTMLElement {
	constructor() {
		super();

		debug("test");

		this.addEventListener("click", (event) => {
			debug("foo");
		});
	}

	connectedCallback() {
		this.render();
	}

	render() {
	}
}

customElements.define("app-shortcut", AppShortcut);
