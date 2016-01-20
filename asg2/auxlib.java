// $Id: auxlib.java,v 1.7 2014-02-04 15:19:48-08 - - $
// Antony Robbins androbbi@ucsc.edu
// Brandon Gomez brlgomez@ucsc.edu
//
// NAME
//    auxlib - Auxiliary miscellanea for handling system interaction.
//
// DESCRIPTION
//    Auxlib has system access functions that can be used by other
//    classes to print appropriate messages and keep track of
//    the program name and exit codes.  It assumes it is being run
//    from a jar and gets the name of the program from the classpath.
//    Can not be instantiated.
//

import static java.lang.System.*;
import static java.lang.Integer.*;
//import linkedqueue.node;

public final class auxlib{
   public static final String PROGNAME =
                 basename (getProperty ("java.class.path"));
   public static final int EXIT_SUCCESS = 0;
   public static final int EXIT_FAILURE = 1;
   public static int exitvalue = EXIT_SUCCESS;

   //
   // private ctor - prevents class from new instantiation.
   //
   private auxlib () {
      throw new UnsupportedOperationException ();
   }

   //
   // basename - strips the dirname and returns only the basename.
   //            See:  man -s 3c basename
   //
   public static String basename (String pathname) {
      if (pathname == null || pathname.length () == 0) return ".";
      String[] paths = pathname.split ("/");
      for (int index = paths.length - 1; index >= 0; --index) {
         if (paths[index].length () > 0) return paths[index];
      }
      return "/";
   }

   //
   // Functions:
   //    whine   - prints a message with a given exit code.
   //    warn    - prints a stderr message and sets the exit code.
   //    die     - calls warn then exits.
   // Combinations of arguments:
   //    objname - name of the object to be printed (optional)
   //    message - message to be printed after the objname,
   //              either a Throwable or a String.
   //
   public static void whine (int exitval, Object... args) {
      exitvalue = exitval;
      err.printf ("%s", PROGNAME);
      for (Object argi : args) err.printf (": %s", argi);
      err.printf ("%n");
   }
   public static void warn (Object... args) {
      whine (EXIT_FAILURE, args);
   }
   public static void die (Object... args) {
      warn (args);
      exit ();
   }

   //
   // usage_exit - prints a usage message and exits.
   //
   public static void usage_exit (String optsargs) {
      exitvalue = EXIT_FAILURE;
      err.printf ("Usage: %s %s%n", PROGNAME, optsargs);
      exit ();
   }

   //
   // exit - calls exit with the appropriate code.
   //        This function should be called instead of returning
   //        from the main function.
   //
   public static void exit () {
      System.exit (exitvalue);
   }

   //
   // identity - returns the default Object.toString value
   //            Useful for debugging.
   //
   public static String identity (Object object) {
      return object == null ? "(null)"
           : object.getClass().getName() + "@"
           + toHexString (identityHashCode (object));
   }

   //
   // caller - return information about the caller of the
   //          function that called this function in the form
   //          filename[linenumber] functionname
   //
   public static String caller() {
      StackTraceElement caller
           = Thread.currentThread().getStackTrace()[2];
      return String.format ("%s[%d] %s", caller.getFileName(),
             caller.getLineNumber(), caller.getMethodName());
   }
   
   public static void printparagraph(linkedqueue<String> queue,
            int lineLength){
      StringBuffer sBuff = new StringBuffer();
      String prev = queue.get();
      if(jroff.format == true){
         sBuff.append(commands.command_po(null, commands.pageOffSet));
      }
      jroff.format = false;
      while(queue.empty() != true){
         jroff.count += queue.get().length();
         if(jroff.count > lineLength){
            sBuff.append("\n");
            sBuff.append(commands.command_po(null,
                     commands.pageOffSet));
            jroff.count = queue.get().length();   
         }
         if(jroff.count > queue.get().length()){
            int last = prev.length() - 1;
            if(prev.charAt(last) == '.' && last != -1){
               sBuff.append("  ");
               jroff.count += 2;
            }
            else{
               sBuff.append(" ");
               jroff.count++;
            }
         }
         prev = queue.get();
         sBuff.append(queue.get());
         queue.remove();
     }
      out.print(sBuff);
      sBuff.setLength(0);
   }
}
