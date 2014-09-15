############################################################################
#                                                                          #
# project name: jasmCompiler                                               #
# release date: September 2014                                             #
#                                                                          #
############################################################################
#                                                                          #
# Java is one of the top programming languages known for its platform      #
# independency, which is reached by using platform specific Java Virtual   #
# Machines (JVM). Each JVM follows strict rules how class files            #
# containing the bytecode are parsed and executed. However, there are no   #
# such rules for the compilation part and the programmer has no influence  #
# on the compiled code.                                                    #
#                                                                          #
# This project is a extended compiler, that in addition to the standard    #
# Java commands, also supports usage of blocks that contain Java bytecode. #
# It uses standard Java compiler to compile the Java part and then inserts #
# the bytecode from the jasm block into the class file.                    #
#                                                                          #
# The project can simply be copied into a folder that is contained in the  #
# local variable PATH and the javacasm becomes a recognized command. This  #
# obviously works for Windows. For other systems adjust the executable     #
# file appropriately. Usage: javacasm <filename.java>                      #
#                                                                          #
# The compiler contains three compilation methods:                         #
# 1: usage of Runtime.getRuntime - requires JDK and works from Java 1.0    #
#    sends specific command to processor (javac -g <filename.java>)!       #
# 2: usage of JavaCompiler - requires JDK and works from Java 1.6          #
# 3: usage of eclipse ecj - only requries JRE and works since Java 1.6     #
#    note that on the newer Java release, ejc jar file should be updated!  #
#                                                                          #
############################################################################