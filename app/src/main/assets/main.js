class AppShortcut extends HTMLElement {
	connectedCallback() {
		this.innerHTML = `<h1>Okay so here's what is up: ${this.getAttribute("lol")}</h1>`;
	}
}

customElements.define("app-shortcut", AppShortcut);
