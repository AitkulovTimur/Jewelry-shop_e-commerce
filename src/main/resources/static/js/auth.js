
async function signUp(email, password) {
    let response = await fetch('http://localhost:9081/shop/auth/sign-up', {method: 'POST', body: {email: email, password: password},
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json, application/xml, text/plain, text/html, *.*',
      },});
    if (response.status == 200) {
        window.location.replace('http://localhost:9081/shop/authentication')
    } else if (response.status == 403) {
        $('.error').text('Пользователей уже существует')
    } else if (response.status == 400) {
        $('.error').text('Неправильный формат логина или пароля')
    }
}

$('.sign-up-button').on('click', () => {
    const password = $('.password').val();
    const email = $('.email').val();
    signUp(email, password);
});

