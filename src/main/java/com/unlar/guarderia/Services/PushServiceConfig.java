package com.unlar.guarderia.Services;

import com.unlar.guarderia.Entitites.PushSubscription;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import java.security.Security;

@Service
public class PushServiceConfig {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final String publicKey = "BInFhl3dZ4VqMQFLM5G4vSG9Gb9Kk14BYFpMliRFYloPuCaRQicLJ1O2eDSOnqkhsz5etNgcjcJz4tShF4a9xts"; 
    private final String privateKey = "aEdLZr7IWEnbGwh3yAap3XgGsM77aIWbK4lj3Au98M0";

    public void sendNotification(PushSubscription sub, String message) {
        try {
            Subscription subscription = new Subscription(
                sub.getEndpoint(), 
                new Subscription.Keys(sub.getP256dh(), sub.getAuth())
            );

            PushService pushService = new PushService(publicKey, privateKey);
            pushService.send(new Notification(subscription, message));
            
        } catch (Exception e) {
            System.err.println("Error enviando Push a " + sub.getEndpoint() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}