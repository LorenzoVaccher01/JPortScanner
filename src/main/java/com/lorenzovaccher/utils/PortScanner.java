/**
 * @author Lorenzo Vaccher
 * Copyright (c) 2021 Lorenzo Vaccher.
 */

package com.lorenzovaccher.utils;

import com.lorenzovaccher.Main;
import com.lorenzovaccher.utils.logger.Color;
import com.lorenzovaccher.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class  is used for handling the connections
 * to the server and to check the ports.
 */
public class PortScanner {
  /**
   * Server's IP address that needs to be tested.
   */
  private String ip;

  /**
   * Number of Threads, and therefore the maximun number of
   * connections that can be established with the server
   * simultaneously.
   */
  private int threads = 200;

  /**
   * Time expressed in milliseconds that automatically 
   * disconnects the Socket.
   */
  private int timeout = 400;

  /**
   * Inital port
   */
  private int portFrom = 1;

  /**
   * End port. If it is set to -1, it means that the user
   * has decided to verify only one port, which is saved 
   * in the "portFrom" variable.
   */
  private int portTo = 65535;

  /**
   * List of all verified ports.
   */
  private List<Future<Port>> ports;

  public PortScanner(String ip) {
    this.ip = ip;
  }

  /**
   * This method is used to start the scanning of the server.
   */
  public void start() {
    Logger.log("Start scanning " + this.ip + "...", Color.CYAN);

    ExecutorService executorService = Executors.newFixedThreadPool(this.threads);
    ports = new ArrayList<>();

    // Adding ports to the list that are verified.
    if (this.portTo != -1)
      for (int i = this.portFrom; i <= this.portTo; i++)
        ports.add(Port.scan(executorService, this.ip, i, this.timeout));
    else
      ports.add(Port.scan(executorService, this.ip, this.portFrom, this.timeout));

    try {
      // This for loop verifies that all Threads have completed their task
      for (Future<Port> port : ports)
        port.get();

      executorService.shutdown();

      // This for loop counts the number of open ports
      int openPorts = 0;
      for (final Future<Port> f : ports) {
        if (f.get().isOpen()) {
          openPorts++;
        }
      }

      Logger.log(Color.CYAN.getColor() + "There are " + Color.YELLOW.getColor() + openPorts + Color.CYAN.getColor() +  " open ports on host " + Color.YELLOW.getColor() + ip);
      Logger.log(Main.SEPARATOR);
    } catch (InterruptedException | ExecutionException ex) {
      ex.printStackTrace();
    }
  }

  public int getThreads() {
    return threads;
  }

  public void setThreads(int threads) {
    this.threads = threads;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public int getPortFrom() {
    return portFrom;
  }

  public void setPortFrom(int portFrom) {
    this.portFrom = portFrom;
  }

  public int getPortTo() {
    return portTo;
  }

  public void setPortTo(int portTo) {
    this.portTo = portTo;
  }
}
