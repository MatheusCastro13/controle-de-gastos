document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#monthYear", {
        plugins: [
            new monthSelectPlugin({
                shorthand: true,            
                dateFormat: "Y-m",           
                altFormat: "F Y",            
                theme: "light"               
            })
        ],
        allowInput: true
    });
});

function filterByDateRange() {
    const startDate = document.getElementById('startDate') ? document.getElementById('startDate').value : '';
    const endDate = document.getElementById('endDate') ? document.getElementById('endDate').value : '';

    const monthYear = document.getElementById('monthYear') ? document.getElementById('monthYear').value : '';

    const url = new URL(window.location.href);

    if (startDate) {
        url.searchParams.set('startDate', startDate);
    } else {
        url.searchParams.delete('startDate');
    }
    if (endDate) {
        url.searchParams.set('endDate', endDate);
    } else {
        url.searchParams.delete('endDate');
    }

    if (monthYear) {
        const [year, month] = monthYear.split('-');
        url.searchParams.set('month', month);
        url.searchParams.set('year', year);
    } else {
        url.searchParams.delete('month');
        url.searchParams.delete('year');
    }

    url.searchParams.set('page', 0);

    window.location.href = url.toString();
}
