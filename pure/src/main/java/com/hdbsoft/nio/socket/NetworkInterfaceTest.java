package com.hdbsoft.nio.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkInterfaceTest {

    public static void main(String[] args) throws SocketException {
        Enumeration enums = NetworkInterface.getNetworkInterfaces();
        while(enums.hasMoreElements()) {
            NetworkInterface net = (NetworkInterface)enums.nextElement();
            System.out.println("Network Interface display name: " + net.getDisplayName());
            System.out.println(net.getDisplayName() + " is up and running? " + net.isUp());
            System.out.println(net.getDisplayName() + " support multicast? " + net.supportsMulticast());
            System.out.println(net.getDisplayName() + " name:  " + net.getName());
            System.out.println(net.getDisplayName() + " is virtual? " + net.isVirtual());

            System.out.println("IP addresses:");
            Enumeration enumIP = net.getInetAddresses();
            while(enumIP.hasMoreElements()) {
                InetAddress ip = (InetAddress)enumIP.nextElement();
                System.out.println("IP address: " + ip);
            }
            System.out.println("");
        }
    }
}
