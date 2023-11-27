
const contain = document.querySelector(".contain");
let loadedScript  = new Set();
async function init() {
    contain.innerHTML = await AJAX('/play');

    const path = '/javascript/fragments/timeSelector.js';
    const script = document.createElement('script');
    const text = document.createTextNode(await AJAX(path));
    script.appendChild(text);
    contain.append(script);

    loadedScript.add(path);
}

init()

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