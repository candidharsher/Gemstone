package com.example.demo;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.List;

public class PayPalPaymentHandler {

	private final String clientId;
	private final String clientSecret;
	private final String mode;
	private final APIContext apiContext;

	public PayPalPaymentHandler(String clientId, String clientSecret, String mode) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.mode = mode;
		this.apiContext = new APIContext(clientId, clientSecret, mode);
	}

	public String crearOrdenDePago(double cantidad) {
		Amount amount = new Amount();
		amount.setCurrency("EUR"); // Establece la moneda
		amount.setTotal(String.valueOf(cantidad)); // Establece la cantidad a pagar

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("Descripción del pago");

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("URL de cancelación");
		redirectUrls.setReturnUrl("URL de retorno");

		payment.setRedirectUrls(redirectUrls);

		try {
			Payment createdPayment = payment.create(apiContext);
			return createdPayment.getId(); // Devuelve el ID de la orden creada
		} catch (PayPalRESTException e) {
			// Maneja los errores adecuadamente
			e.printStackTrace();
			return null;
		}
	}

	public void capturarPago(String orderId) {
		Payment payment = new Payment();
		payment.setId(orderId);

		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId("payer_id"); // Establece el ID del pagador

		try {
			Payment executedPayment = payment.execute(apiContext, paymentExecution);
			// Maneja la respuesta después de capturar el pago
		} catch (PayPalRESTException e) {
			// Maneja los errores adecuadamente
			e.printStackTrace();
		}
	}

	public String obtenerURLPayPal(String orderId) {
		Payment payment = new Payment();
		payment.setId(orderId);

		try {
			Payment executedPayment = payment.get(apiContext, orderId);
			List<Links> links = executedPayment.getLinks();
			for (Links link : links) {
				if (link.getRel().equalsIgnoreCase("approval_url")) {
					return link.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void abrirNavegadorConURL(String urlPayPal) {
		try {
			// Abre el navegador con la URL de PayPal
			// (Dependiendo del sistema operativo, puedes usar diferentes enfoques para
			// abrir el navegador)
			java.awt.Desktop.getDesktop().browse(new java.net.URI(urlPayPal));
		} catch (java.io.IOException | java.net.URISyntaxException e) {
			e.printStackTrace();
		}
	}
	public String obtenerURLAutorizacion(String orderId) {
        String redirectURL = "";
        try {
            String redirectURI = "https://tu_uri_de_redireccion.com"; // Reemplaza con tu URI de redirección

            // Construir la URL de autorización
            String authorizationURL = "https://www.sandbox.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize" +
                    "?client_id=" + clientId +
                    "&response_type=code" +
                    "&scope=openid+profile+email" +
                    "&redirect_uri=" + redirectURI +
                    "&nonce=" + orderId; // Utiliza el orderId como nonce

            redirectURL = authorizationURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redirectURL;
    }
}
