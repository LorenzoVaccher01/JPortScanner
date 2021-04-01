/**
 * @author Lorenzo Vaccher
 * Copyright (c) 2021 Lorenzo Vaccher.
 */

package com.lorenzovaccher;

import com.lorenzovaccher.utils.PortScanner;
import com.lorenzovaccher.utils.WellKnownPort;
import com.lorenzovaccher.utils.logger.Color;
import com.lorenzovaccher.utils.logger.Logger;

import org.apache.commons.cli.*;
import java.net.*;

public class Main {

  public static final String VERSION = "v0.0.1";
  public static final String SEPARATOR = Color.WHITE_BOLD.getColor() + "-------------------------------------------------------------" + Color.RESET.getColor();

  public static void main(String[] args) {
    // Logger initialization
    new Logger();
    Logger.clearConsole();
    Logger.log(SEPARATOR);
    Logger.log("Running PortScanner " + VERSION, Color.MAGENTA);
    Logger.log("Checking parameters...", Color.YELLOW);

    Options options = new Options();
    // IPV4 parameter
    Option hostOption = new Option("h", "host", true, "The host of the machine to be tested.");
    hostOption.setRequired(true);
    options.addOption(hostOption);
    // Threads parameter
    Option threadsOption = new Option("th", "threads", true, "Number of threads the program can execute.");
    options.addOption(threadsOption);
    // Timeout parameter
    Option timeoutOption = new Option("t", "timeout", true, "Time in milliseconds for the socket to disconnect");
    options.addOption(timeoutOption);
    // Port(s) parameter
    Option portsOption = new Option("p", "ports", true, "Port range to scan");
    options.addOption(portsOption);

    //header of the menu section
    String header = "";
    //footer of the menu section
    String footer = "\nYou can report bugs through GitHub: \nSoftware created by Lorenzo Vaccher."; //todo: inserire il link di GitHub

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("portscanner", header, options, footer, true);
      System.exit(1);
    }

    // Check data validity
    if (checkIp(cmd.getOptionValue("host"))) {
      try {
        final InetAddress inetAddress = InetAddress.getByName(cmd.getOptionValue("host"));
        Logger.log("The host is reachable (" + inetAddress.getHostAddress() + ")!", Color.GREEN);
        PortScanner portScanner = new PortScanner(inetAddress.getHostAddress());

        if (cmd.getOptionValue("threads") != null)
          portScanner.setThreads(Integer.parseInt(cmd.getOptionValue("threads")));

        if (cmd.getOptionValue("timeout") != null)
          portScanner.setTimeout(Integer.parseInt(cmd.getOptionValue("timeout")));

        if (cmd.getOptionValue("ports") != null)
          if (cmd.getOptionValue("ports").contains("-")) {
            int portFrom = Integer.parseInt(cmd.getOptionValue("ports").split("-")[0]);
            int portTo = Integer.parseInt(cmd.getOptionValue("ports").split("-")[1]);
            if (portFrom < portTo) {
              portScanner.setPortFrom(portFrom);
              portScanner.setPortTo(portTo);
            } else if (portFrom > portTo) {
              portScanner.setPortFrom(portTo);
              portScanner.setPortTo(portFrom);
            } else {
              portScanner.setPortFrom(portFrom);
              portScanner.setPortTo(-1);
            }
          } else {
            portScanner.setPortFrom(Integer.parseInt(cmd.getOptionValue("ports")));
            portScanner.setPortTo(-1); //-1 indicates that there is no end
          }

        // Loading of known ports
        WellKnownPort.load();
        // Start scan
        portScanner.start();
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This Function is used to check the validity of an IPV4 address
   * or of a HostName.
   *
   * @param ip The ip or the name of the host that needs to be verified
   * @return Authenticity of the ip passed down as an input to the function
   */
  private static boolean checkIp(String ip) {
    boolean isIPv4;
    try {
      final InetAddress inetAddress = InetAddress.getByName(ip);
      isIPv4 = (inetAddress.getHostAddress().equals(ip) || inetAddress.getHostName().equals(ip))
              && inetAddress instanceof Inet4Address;
    } catch (final UnknownHostException e) {
      Logger.log("Host unreachable...", Color.RED);
      return false;
    }
    return isIPv4;
  }
}