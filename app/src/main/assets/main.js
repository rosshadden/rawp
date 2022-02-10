if (typeof window.rawp === "undefined") {
	window.rawp = {
		getIcon() {
			return "https://placedog.net/100/100";
		}
	};
}

class AppShortcut extends HTMLElement {
	constructor() {
		super();

		this.id = this.getAttribute("id");

		this.addEventListener("click", (event) => {
			rawp.launch(this.id);
		});
	}

	connectedCallback() {
		this.render();
	}

	render() {
		const body = this.innerHTML;
		const icon = rawp.getIcon(this.id);

		this.innerHTML = `
			<img src="${icon}">
			<span>${body}</span>
		`;
	}
}

customElements.define("app-shortcut", AppShortcut);
