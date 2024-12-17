document.addEventListener('DOMContentLoaded', function () {
      MicroModal.init();
    });
      

document.addEventListener('DOMContentLoaded', function() {
    const table = document.getElementById('roadmap-table');
    table.addEventListener('click', function(e) {
      const target = e.target.closest('tr');
      if (target && target.dataset.href) {
        window.location.href = target.dataset.href;
      }
    });
  });
  
  
  