package mx.com.wcontact.poderjudicial.util;

import static java.lang.StringTemplate.STR;

public final class EmailTemplate {

    public static String Coincidencia(String title,String name, String fechaPublicacion, String fechaAcuerdo, String expediente, String boletin, String actNames, String demNames) {
        return STR."""
      <!DOCTYPE html>
            <html>
                <head>
                    <title>\{title}</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #F7F7F7;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            width: 80%;
                            margin: 0 auto;
                            background-color: #FFF;
                            padding: 20px;
                        }
                        .header {
                            text-align: center;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Se ha encontrado una coincidencia en el Boletin del Poder Judicial!</h1>
                        </div>

                        <p>Hola, \{name}!</p>
                        <p>Hemos encontrado una coincidencia en la revision del Boletin del Poder Judicial del Estado de jalisco.</p>
                        <p>Expediente \{expediente}.</p>
                        <p>Demandantes \{actNames}.</p>
                        <p>Demandado \{demNames}.</p>
                        <p>Fecha de Publicacion: \{fechaPublicacion} y Fecha de Acuerdo: \{fechaAcuerdo}.</p>

                        <h3> Boletin: </h3>
                        <p> \{boletin} </p>

                        <div class="footer">
                            <p>Best Regards,</p>
                            <p>Web Contact, Team.</p>
                        </div>
                    </div>
                </body>
            </html>
      """;
    }

    public static String ContactoRegistrado(String title,String name, String subject,String mobile, String email, String comment) {
        return STR."""
      <!DOCTYPE html>
            <html>
                <head>
                    <title>\{title}</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #F7F7F7;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            width: 80%;
                            margin: 0 auto;
                            background-color: #FFF;
                            padding: 20px;
                        }
                        .header {
                            text-align: center;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Tu Contacto ha sido registrado!</h1>
                        </div>

                        <p>Hola, \{name}!</p>
                        <p>Hemos realizado tu contacto y enviado la informacion al area correspondiente.</p>
                        <p>Espera un nuevo correo del area de \{subject}.</p>
                        <p>Tus Datos de Contacto Proporcionado son: \{email} y tu Numero Movil: \{mobile}.</p>

                        <h3> Comentarios: </h3>
                        <p> \{comment} </p>

                        <div class="footer">
                            <p>Best Regards,</p>
                            <p>Web Contact, Team.</p>
                        </div>
                    </div>
                </body>
            </html>
      """;
    }

    public static String RecuperaPassword(String title,String firstName, String lastName, String accountName, String password) {
        return STR."""
      <!DOCTYPE html>
            <html>
                <head>
                    <title>\{title}</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #F7F7F7;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            width: 80%;
                            margin: 0 auto;
                            background-color: #FFF;
                            padding: 20px;
                        }
                        .header {
                            text-align: center;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Bienvenido a Nuestra Aplicacion!</h1>
                        </div>

                        <p>Hola, \{firstName} \{lastName}!</p>
                        <p>Se ha solicitado la recuperacion de tu Password.</p>
                        <p>A continuacion te compartimos tus datos de ingreso</p>
                        <p>Recuerda que tu usuario de acceso es: \{accountName} y tu Password es: \{password}.</p>

                        <div class="footer">
                            <p>Best Regards,</p>
                            <p>Web Contact, Team.</p>
                        </div>
                    </div>
                </body>
            </html>
      """;
    }

}
