/**
 * Copyright (c) 2004 - 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclim.plugin.jdt.command.classpath;

import java.util.Iterator;
import java.util.List;

import org.eclim.command.OutputFilter;

/**
 * Filter for ClasspathVariablesCommand.
 *
 * @author Eric Van Dewoestine (ervandew@yahoo.com)
 * @version $Revision$
 */
public class ClasspathVariablesFilter
  implements OutputFilter
{
  /**
   * {@inheritDoc}
   */
  public String filter (Object _result)
  {
    StringBuffer buffer = new StringBuffer();

    List list = (List)_result;
    for(Iterator ii = list.iterator(); ii.hasNext();){
      ClasspathVariable variable = (ClasspathVariable)ii.next();
      if(buffer.length() > 0){
        buffer.append('\n');
      }
      buffer.append(variable.getName())
        .append(" - ")
        .append(variable.getPath());
    }
    return buffer.toString();
  }
}
