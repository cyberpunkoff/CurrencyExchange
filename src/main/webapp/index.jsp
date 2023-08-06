<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!-- saved from url=(0082)https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/ -->
<html lang="en-US"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Begin Jekyll SEO tag v2.8.0 -->
  <title>Проект “Обмен валют” | java-backend-learning-course</title>
  <meta name="generator" content="Jekyll v3.9.3">
  <meta property="og:title" content="Проект “Обмен валют”">
  <meta property="og:locale" content="en_US">
  <link rel="canonical" href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/">
  <meta property="og:url" content="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/">
  <meta property="og:site_name" content="java-backend-learning-course">
  <meta property="og:type" content="website">
  <meta name="twitter:card" content="summary">
  <meta property="twitter:title" content="Проект “Обмен валют”">
  <script type="application/ld+json">
    {"@context":"https://schema.org","@type":"WebPage","headline":"Проект “Обмен валют”","url":"https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/"}</script>
  <!-- End Jekyll SEO tag -->

  <style class="anchorjs"></style><link rel="stylesheet" href="https://zhukovsd.github.io/java-backend-learning-course/assets/css/style.css">
  <!-- start custom head snippets, customize with your own _includes/head-custom.html file -->

  <!-- Setup Google Analytics -->



  <!-- You can set your favicon here -->
  <!-- link rel="shortcut icon" type="image/x-icon" href="/java-backend-learning-course/favicon.ico" -->

  <!-- end custom head snippets -->

</head>
<body>
<div class="container-lg px-3 my-5 markdown-body">

  <h2 id="проект-обмен-валют">Проект “Обмен валют”<a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#%D0%BF%D1%80%D0%BE%D0%B5%D0%BA%D1%82-%D0%BE%D0%B1%D0%BC%D0%B5%D0%BD-%D0%B2%D0%B0%D0%BB%D1%8E%D1%82" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h2>

  <p>REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и совершать расчёт конвертации произвольных сумм из одной валюты в другую.</p>

  <h2 id="rest-api">REST API<a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#rest-api" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h2>

  <p>Методы REST API реализуют <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">CRUD</a> интерфейс над базой данных - позволяют создавать (C - create), читать (R - read), редактировать (U - update). В целях упрощения, опустим удаление (D - delete).</p>

  <h3 id="валюты">Валюты<a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#%D0%B2%D0%B0%D0%BB%D1%8E%D1%82%D1%8B" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h3>

  <h4 id="get-currencies">GET <code class="language-plaintext highlighter-rouge">/currencies</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#get-currencies" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Получение списка валют. Пример ответа:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h4 id="get-currencyeur">GET <code class="language-plaintext highlighter-rouge">/currency/EUR</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#get-currencyeur" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Получение конкретной валюты. Пример ответа:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Код валюты отсутствует в адресе - 400</li>
    <li>Валюта не найдена - 404</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h4 id="post-currencies">POST <code class="language-plaintext highlighter-rouge">/currencies</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#post-currencies" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Добавление новой валюты в базу. Данные передаются в теле запроса в виде полей формы (<code class="language-plaintext highlighter-rouge">x-www-form-urlencoded</code>). Поля формы - <code class="language-plaintext highlighter-rouge">name</code>, <code class="language-plaintext highlighter-rouge">code</code>, <code class="language-plaintext highlighter-rouge">sign</code>. Пример ответа - JSON представление вставленной в базу записи, включая её ID:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Отсутствует нужное поле формы - 400</li>
    <li>Валюта с таким кодом уже существует - 409</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h3 id="обменные-курсы">Обменные курсы<a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#%D0%BE%D0%B1%D0%BC%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5-%D0%BA%D1%83%D1%80%D1%81%D1%8B" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h3>

  <h4 id="get-exchangerates">GET <code class="language-plaintext highlighter-rouge">/exchangeRates</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#get-exchangerates" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Получение списка всех обменных курсов. Пример ответа:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>[
    {
        "id": 0,
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h4 id="get-exchangerateusdrub">GET <code class="language-plaintext highlighter-rouge">/exchangeRate/USDRUB</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#get-exchangerateusdrub" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Пример ответа:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}

</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Коды валют пары отсутствуют в адресе - 400</li>
    <li>Обменный курс для пары не найден - 404</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h4 id="post-exchangerates">POST <code class="language-plaintext highlighter-rouge">/exchangeRates</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#post-exchangerates" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Добавление нового обменного курса в базу. Данные передаются в теле запроса в виде полей формы (<code class="language-plaintext highlighter-rouge">x-www-form-urlencoded</code>). Поля формы - <code class="language-plaintext highlighter-rouge">baseCurrencyCode</code>, <code class="language-plaintext highlighter-rouge">targetCurrencyCode</code>, <code class="language-plaintext highlighter-rouge">rate</code>. Пример полей формы:</p>
  <ul>
    <li><code class="language-plaintext highlighter-rouge">baseCurrencyCode</code> - USD</li>
    <li><code class="language-plaintext highlighter-rouge">targetCurrencyCode</code> - EUR</li>
    <li><code class="language-plaintext highlighter-rouge">rate</code> - 0.99</li>
  </ul>

  <p>Пример ответа - JSON представление вставленной в базу записи, включая её ID:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Отсутствует нужное поле формы - 400</li>
    <li>Валютная пара с таким кодом уже существует - 409</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h4 id="patch-exchangerateusdrub">PATCH <code class="language-plaintext highlighter-rouge">/exchangeRate/USDRUB</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#patch-exchangerateusdrub" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Обновление существующего в базе обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Данные передаются в теле запроса в виде полей формы (<code class="language-plaintext highlighter-rouge">x-www-form-urlencoded</code>). Единственное поле формы - <code class="language-plaintext highlighter-rouge">rate</code>.</p>

  <p>Пример ответа - JSON представление обновлённой записи в базе данных, включая её ID:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}

</code></pre></div></div>

  <p>HTTP коды ответов:</p>
  <ul>
    <li>Успех - 200</li>
    <li>Отсутствует нужное поле формы - 400</li>
    <li>Валютная пара отсутствует в базе данных - 404</li>
    <li>Ошибка (например, база данных недоступна) - 500</li>
  </ul>

  <h3 id="обмен-валюты">Обмен валюты<a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#%D0%BE%D0%B1%D0%BC%D0%B5%D0%BD-%D0%B2%D0%B0%D0%BB%D1%8E%D1%82%D1%8B" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h3>

  <h4 id="get-exchangefrombase_currency_codetotarget_currency_codeamountamount">GET <code class="language-plaintext highlighter-rouge">/exchange?from=BASE_CURRENCY_CODE&amp;to=TARGET_CURRENCY_CODE&amp;amount=$AMOUNT</code><a class="anchorjs-link " href="https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/#get-exchangefrombase_currency_codetotarget_currency_codeamountamount" aria-label="Anchor" data-anchorjs-icon="" style="font: 1em / 1 anchorjs-icons; padding-left: 0.375em;"></a></h4>

  <p>Расчёт перевода определённого количества средств из одной валюты в другую. Пример запроса - GET <code class="language-plaintext highlighter-rouge">/exchange?from=USD&amp;to=AUD&amp;amount=10</code>.</p>

  <p>Пример ответа:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00
    "convertedAmount": 14.50
}
</code></pre></div></div>

  <p>Получение курса для обмена может пройти по одному из трёх сценариев. Допустим, совершаем перевод из валюты <strong>A</strong> в валюту <strong>B</strong>:</p>
  <ol>
    <li>В таблице <code class="language-plaintext highlighter-rouge">ExchangeRates</code> существует валютная пара <strong>AB</strong> - берём её курс</li>
    <li>В таблице <code class="language-plaintext highlighter-rouge">ExchangeRates</code> существует валютная пара <strong>BA</strong> - берем её курс, и считаем обратный, чтобы получить <strong>AB</strong></li>
    <li>В таблице <code class="language-plaintext highlighter-rouge">ExchangeRates</code> существуют валютные пары <strong>USD-A</strong> и <strong>USD-B</strong> - вычисляем из этих курсов курс <strong>AB</strong></li>
  </ol>

  <p>Остальные возможные сценарии, для упрощения, опустим.</p>

  <hr>

  <p>Для всех запросов, в случае ошибки, ответ может выглядеть так:</p>
  <div class="language-plaintext highlighter-rouge"><div class="highlight"><pre class="highlight"><code>{
    "message": "Валюта не найдена"
}
</code></pre></div></div>

  <p>Значение <code class="language-plaintext highlighter-rouge">message</code> зависит от того, какая именно ошибка произошла.</p>

</body></html>