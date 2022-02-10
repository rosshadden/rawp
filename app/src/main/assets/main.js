class AppShortcut extends HTMLElement {
	constructor() {
		super();

		this.addEventListener("click", (event) => {
			rawp.launch(this.getAttribute("id"));
		});
	}

	connectedCallback() {
		this.render();
	}

	render() {
	}
}

customElements.define("app-shortcut", AppShortcut);
