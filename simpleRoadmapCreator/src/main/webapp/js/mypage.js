document.addEventListener('DOMContentLoaded', function() {
	 MicroModal.init();
	
    const table = document.getElementById('roadmap-table');
    table.addEventListener('click', function(e) {
      const target = e.target.closest('tr');
      if (target && target.dataset.href) {
        window.location.href = target.dataset.href;
      }
    });
    
    const checkbox = document.getElementById('delete-confirm');
	const deleteButton = document.getElementById('delete-confirm-button');

	checkbox.addEventListener('change', function() {
  	if (this.checked) {
	    deleteButton.disabled = false;
	} else {
	    deleteButton.disabled = true;
  	}
  	});
});
  
  
  