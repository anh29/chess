const contain = document.querySelector(".contain");

async function init() {
    contain.innerHTML=await AJAX('/play')
}

async function AJAX(fragment, json = false) {
    // var token = sessionStorage.getItem('jwtToken');
    // if (token) {
    //     $.ajaxSetup({
    //         headers: {
    //             Authorization: 'Bearer ' + token,
    //         },
    //     });
    // }
    // console.log('Token: ', token);

    try {
        const response = await fetch(fragment, {
            method: 'GET',
            headers: {
                'request-source': 'JS',
            },
        });

        const data = json ? await response.json() : await response.text();

        if (!response.ok) throw new Error('Error response');
        return data;
    } catch (e) {
        console.error(e.message);
    }
}