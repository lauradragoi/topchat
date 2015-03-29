# Introduction #

This page describes the requirements of the software.


# First version #

Communication requirements:
  1. the server should support minimum 100 of simultaneous client connections;

Client requirements:
  1. a window with a primary view and alternate views;
  1. primary view: a display panel with all the utterances, a text area and a send button;
  1. possibility to add a reference (a link) to a previous message from the conversation;
  1. alternate view 1: threaded chat accordingly to the references;
  1. possibility to create a poll and vote in a poll;

Server requirements:
  1. store all the client messages (utterances and control messages like a reference) in a database (or XML file);
  1. store the polls and the votes in a database (or XML file)

Design questions:
  1. which protocol to use? an existing one or a new one? keep in mind the multi-user chat and the need vor various control messages;
  1. storing the information: DB or XML;
  1. communication: java sockets;