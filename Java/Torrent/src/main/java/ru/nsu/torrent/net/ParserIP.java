package ru.nsu.torrent.net;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public abstract class ParserIP {
    public static String getIP(String nameHost) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.getName().startsWith(nameHost)) { // имя адаптера беспроводной локальной сети начинается с "w"
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet4Address) { // выбираем IPv4-адрес
                            return address.getHostAddress();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error to get IP address");
        }
        return null;
    }
}
