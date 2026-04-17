package plataforma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import plataforma.domain.Usuario;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Enviar correo de confirmación al crear usuario
    public void enviarCorreoConfirmacion(Usuario usuario) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(usuario.getEmail());
            mensaje.setSubject("¡Bienvenido a la Plataforma de Reservas de Eventos!");
            mensaje.setText("Hola " + usuario.getNombre() + ",\n\n" +
                    "Tu cuenta ha sido creada exitosamente en nuestra plataforma.\n" +
                    "Email: " + usuario.getEmail() + "\n" +
                    "Rol: " + usuario.getRol().getNombre() + "\n\n" +
                    "Gracias por registrarte.\n\n" +
                    "Saludos,\n" +
                    "Equipo de Plataforma de Eventos");

            mailSender.send(mensaje);
            System.out.println("Correo enviado exitosamente a: " + usuario.getEmail());
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Enviar correo de notificación genérica
    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);

            mailSender.send(mensaje);
            System.out.println("Correo enviado exitosamente a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Enviar correo de evento
    public void enviarCorreoEvento(String destinatario, String nombreEvento, String fecha) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject("Notificación: Nuevo Evento");
            mensaje.setText("Se ha creado un nuevo evento:\n\n" +
                    "Nombre: " + nombreEvento + "\n" +
                    "Fecha: " + fecha + "\n\n" +
                    "¡No olvides registrarte!");

            mailSender.send(mensaje);
            System.out.println("Correo de evento enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de evento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}