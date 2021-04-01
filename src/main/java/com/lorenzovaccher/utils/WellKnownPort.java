/**
 * @author Lorenzo Vaccher
 * Copyright (c) 2021 Lorenzo Vaccher.
 */

package com.lorenzovaccher.utils;

import com.lorenzovaccher.utils.logger.Color;
import com.lorenzovaccher.utils.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used for handling the ports associated 
 * with known protocols.
 */
public class WellKnownPort {
  /**
   * This protocol is associated with the port. (TCP or UDP)
   */
  private String protocol;

  /**
   * Name of the protocol
   */
  private String name;

  /**
   * Port
   */
  private int port;

  private static List<WellKnownPort> wellKnownPorts = new ArrayList<>();

  /**
   * Constructor of the class
   *
   * @param protocol The protocol associated with the port
   * @param port Scanned port
   * @param name name of the protocol
   */
  WellKnownPort(String protocol, int port, String name) {
    this.protocol = protocol;
    this.port = port;
    this.name = name;
  }

  /**
  * This function is used to load all known ports
  * from the "ports.csv" file. These ports are loaded into the list
  * wellKnownPorts, which is used to associate the open port's number
  * of a server to its protocol.
  */
  public static void load() {
    Logger.log("Loading WellKnown ports...", Color.YELLOW);
    try {
      Scanner scanner = new Scanner(new File("src/main/resources/ports.csv"));
      scanner.nextLine(); //skips first line

      // Lettura del file ports.csv
      while (scanner.hasNextLine()){
        final String line = scanner.nextLine();
        String[] comps = line.split(",");

        // Adding the port to the list
        wellKnownPorts.add(new WellKnownPort(comps[0].replace("\"", ""), Integer.parseInt(comps[1]), comps[2].replace("\"", "")));
      }

      // Closing the scanner
      scanner.close();
      Logger.log("All WellKnown ports have been loaded", Color.GREEN);
    } catch (Exception e) {
      Logger.log("It was not possible to load WellKnownPorts", Color.RED);
      e.printStackTrace();
      System.exit(-1);
    }
  }

  /**
   *
   * @param port port number that needs to be check
   * @return WellKnownPort is associated with the port 
   *         passed down as a method's parameter
   */
  public static WellKnownPort getByPortNumber(int port) {
    for (WellKnownPort wellKnownPort : wellKnownPorts) {
      if (wellKnownPort.getPort() == port)
        return wellKnownPort;
    }
    return new WellKnownPort(null, -1,"Unknown");
  }

  /**
   *
   * @param name name to search for
   * @return WellKnownPort is associated with the name 
              passed down as a method's argument
   */
  public static WellKnownPort getByName(String name) {
    for (WellKnownPort wellKnownPort : wellKnownPorts) {
      if (wellKnownPort.getName().equals(name))
        return wellKnownPort;
    }
    return new WellKnownPort(null, -1,"Unknown");
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}