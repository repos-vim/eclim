/**
 * Copyright (c) 2004
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.eclim.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import org.apache.commons.collections.CollectionUtils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import org.eclim.Services;

/**
 * Class for defining and working with all the eclim command
 * line options.
 *
 * @author Eric Van Dewoestine (ervandew@yahoo.com)
 * @version $Revision$
 */
public class Options
{
  public static final String COMMAND_OPTION = "command";
  public static final String FILTER_OPTION = "filter";

  public static final String APPLY_OPTION = "a";
  public static final String BASETYPE_OPTION = "b";
  public static final String BUILD_FILE_OPTION = "b";
  public static final String CLASSNAME_OPTION = "c";
  public static final String CONTEXT_OPTION = "x";
  public static final String DEPENDS_OPTION = "d";
  public static final String FILE_OPTION = "f";
  public static final String FOLDER_OPTION = "f";
  public static final String HELP = "help";
  public static final String HELP_OPTION = "?";
  public static final String JARS_OPTION = "j";
  public static final String LENGTH_OPTION = "l";
  public static final String LINE_OPTION = "l";
  public static final String METHOD_OPTION = "m";
  public static final String NAME_OPTION = "n";
  public static final String OFFSET_OPTION = "o";
  public static final String PATTERN_OPTION = "p";
  public static final String PORT_OPTION = "port";
  public static final String PROJECT_OPTION = "p";
  public static final String PROPERTIES_OPTION = "p";
  public static final String SCOPE_OPTION = "s";
  public static final String SETTINGS_OPTION = "s";
  public static final String SETTING_OPTION = "s";
  public static final String SOURCE_OPTION = "s";
  public static final String SUPERTYPE_OPTION = "s";
  public static final String TEMPLATE_OPTION = "t";
  public static final String TYPE_OPTION = "t";
  public static final String VALIDATE_OPTION = "v";
  public static final String VALUES_OPTION = "v";

  private static final String ARG = "ARG";
  private static final String REQUIRED = "REQUIRED";
  private static final String OPTION_SUFFIX = ".options";

  private static org.apache.commons.cli.Options coreOptions =
      new org.apache.commons.cli.Options();
  static {
    coreOptions.addOption(OptionBuilder.withArgName(HELP)
        .withLongOpt(HELP)
        .withDescription(Services.getMessage("help.description"))
        .create(HELP_OPTION));
    coreOptions.addOption(OptionBuilder.withArgName(PORT_OPTION)
        .hasArg()
        .withDescription(Services.getMessage("port.description"))
        .create(PORT_OPTION));
    coreOptions.addOption(OptionBuilder.withArgName(COMMAND_OPTION)
        .hasArg()
        .withDescription(Services.getMessage("command.description"))
        .create(COMMAND_OPTION));
    coreOptions.addOption(OptionBuilder.withArgName(FILTER_OPTION)
        .hasArg()
        .withDescription(Services.getMessage("filter.description"))
        .create(FILTER_OPTION));
  }

  protected org.apache.commons.cli.Options options =
    new org.apache.commons.cli.Options();

  /**
   * The user supplied command line args.
   */
  protected CommandLine commandLine;

  /**
   * Creates and initializes the jmpc command line options.
   */
  public Options ()
  {
    for(Iterator ii = coreOptions.getOptions().iterator(); ii.hasNext();){
      options.addOption((Option)ii.next());
    }
  }

  /**
   * Print usage information.
   */
  public void usage ()
  {
    usageSummary();
    System.out.println(buildFooter());
  }

  /**
   * Print summary usage information.
   */
  public void usageSummary ()
  {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(Services.getMessage("usage"), "", coreOptions, null);
  }

  /**
   * Parses the supplied command line options.
   *
   * @param _args The arguments.
   *
   * @return The command line.
   */
  public CommandLine parse (String[] _args)
    throws ParseException
  {
    // manually parse out the command option value so that the command specific
    // options can be added before running the automated parse.
    String command = null;
    for (int ii = 0; ii < _args.length; ii++){
      if(_args[ii].equals('-' + COMMAND_OPTION)){
        if(_args.length > ii + 1){
          command = _args[ii + 1].trim();
        }
        break;
      }
    }
    if(command != null){
      String messageKey = command + OPTION_SUFFIX;
      String optionsString = Services.getMessage(messageKey);
      if(messageKey.equals(optionsString)){
        throw new IllegalArgumentException(
            Services.getMessage("command.not.found", command));
      }
      Collection commandOptions = parseOptions(optionsString);
      for(Iterator iterator = commandOptions.iterator(); iterator.hasNext();){
        options.addOption((Option)iterator.next());
      }
    }

    CommandLineParser parser = new GnuParser();
    return new CommandLine(parser.parse(options, _args));
  }

  /**
   * Builds the usage footer.
   *
   * @return The footer.
   */
  protected String buildFooter ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(Services.getMessage("command.usage.header"));

    TreeSet set = new TreeSet();
    ResourceBundle resources = Services.getResourceBundle();
    for(Enumeration keys = resources.getKeys(); keys.hasMoreElements();){
      String key = (String)keys.nextElement();
      if(key.endsWith(".usage")){
        set.add(resources.getString(key));
      }
    }

    for(Iterator ii = set.iterator(); ii.hasNext();){
      buffer.append("\n").append(ii.next()).append("\n");
    }

    return buffer.toString();
  }

  /**
   * Parses the String representation of the options to a Collection of Options.
   *
   * @param _options The options String.
   * @return The Collection of Option instances.
   */
  protected Collection parseOptions (String _options)
  {
    ArrayList options = new ArrayList();
    if(_options != null && _options.trim().length() > 0){
      String[] lines = StringUtils.split(_options, ',');
      for(int ii = 0; ii < lines.length; ii++){
        if(lines[ii].trim().length() > 0){
          options.add(parseOption(lines[ii].trim()));
        }
      }
    }

    return options;
  }

  /**
   * Parses the String representation of an Option to an Option instance.
   *
   * @param _option The option String.
   * @return The Option.
   */
  protected Option parseOption (String _option)
  {
    String[] parts = StringUtils.split(_option);

    if(REQUIRED.equals(parts[0])){
      OptionBuilder.isRequired();
    }
    if(ARG.equals(parts[3])){
      OptionBuilder.hasArg();
      OptionBuilder.withArgName(parts[2]);
    }
    OptionBuilder.withLongOpt(parts[2]);
    return OptionBuilder.create(parts[1]);
  }
}
