/** Dispatch event on click outside of node */
export function clickOutside(node: Element) {
  
  const handleClick = (event: Event) => {
    if (event.target instanceof Element && node && !node.contains(event.target) && !event.defaultPrevented) {
      node.dispatchEvent(
        new CustomEvent('click_outside', node)
      )
    }
  }

	document.addEventListener('click', handleClick, true);
  
  return {
    destroy() {
      document.removeEventListener('click', handleClick, true);
    }
	}
}