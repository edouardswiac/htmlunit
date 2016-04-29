/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests that the events triggered in the right order.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class Event2Test extends WebDriverTestCase {

    /**
     * Test event order for clicking on a select option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Event] change b:true c:false [select] [-]",
                "[object MouseEvent] click b:true c:true [clickMe] [1]" },
            IE = { "[object Event] change b:true c:false [select] [-]",
                "[object MouseEvent] click b:true c:true [select] [1]" })
    @BuggyWebDriver({ CHROME, FF })
    // FFDriver wrongly generates a "[object MouseEvent] click b:true c:true [select] [1]" first that doesn't occur
    // manually
    // ChromeDriver wrongly generates a "[object MouseEvent] click b:true c:true [select] [1]" instead of "clickMe"
    @NotYetImplemented(IE)
    // No idea why the IE11 fires a MouseEvent here instead of a PointerEvent
    public void optionClick() throws Exception {
        final String firstSnippet = "       <select name='select' id='select' size='2'\n";
        final String secondSnippet = ">\n"
                + "               <option id='o_id1' value='o_value1'>option1</option>\n"
                + "               <option id='clickMe' value='o_value2'>option2</option>\n"
                + "               <option id='o_id3' value='o_value3'>option3</option>\n"
                + "       </select>\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a select option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "")
    @BuggyWebDriver(CHROME)
    // ChromeDriver does not generate a "[object MouseEvent] click b:true c:true [clickMe] [1]" but it occurs manually
    public void optionClick2() throws Exception {
        final String firstSnippet = "       <select name='select' id='select' size='2'>\n"
                + "               <option id='o_id1' value='o_value1'>option1</option>\n"
                + "               <option id='clickMe' value='o_value2'\n";
        final String secondSnippet = ">option2</option>\n"
                + "               <option id='o_id3' value='o_value3'>option3</option>\n"
                + "       </select>\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a radio button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = { "[object MouseEvent] click b:true c:true [radio] [1]",
                "[object Event] change b:true c:false [radio] [-]" },
            FF = { "[object MouseEvent] click b:true c:true [radio] [1]",
                "[object Event] change b:true c:false [radio] [-]" },
            IE = { "[object Event] change b:true c:false [radio] [-]",
                "[object PointerEvent] click b:true c:true [radio] [1]" })
    public void radioClick() throws Exception {
        final String firstSnippet = "       <input type='radio' name='radio' id='clickMe' value='2'\n";
        final String secondSnippet = ">Radio\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a check box.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = { "[object MouseEvent] click b:true c:true [checkbox] [1]",
                "[object Event] change b:true c:false [checkbox] [-]" },
            FF = { "[object MouseEvent] click b:true c:true [checkbox] [1]",
                "[object Event] change b:true c:false [checkbox] [-]" },
            IE = { "[object Event] change b:true c:false [checkbox] [-]",
                "[object PointerEvent] click b:true c:true [checkbox] [1]" })
    public void checkboxClick() throws Exception {
        final String firstSnippet = "       <input type='checkbox' name='checkbox' id='clickMe' value='2'\n";
        final String secondSnippet = ">Checkbox\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a entry field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "[object PointerEvent] click b:true c:true [clickMe] [1]")
    public void inputTextClick() throws Exception {
        final String firstSnippet = "       <input type='text' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a password field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "[object PointerEvent] click b:true c:true [clickMe] [1]")
    public void inputPasswordClick() throws Exception {
        final String firstSnippet = "       <input type='password' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a text area.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "[object PointerEvent] click b:true c:true [clickMe] [1]")
    public void textareaClick() throws Exception {
        final String firstSnippet = "       <textarea name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = "></textarea>\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a submit button.
     * @throws Exception if the test fails
     */
    @Test
    public void submitClick() throws Exception {
        final String firstSnippet = "       <input type='submit' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a reset button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "[object PointerEvent] click b:true c:true [clickMe] [1]")
    public void resetClick() throws Exception {
        final String firstSnippet = "       <input type='reset' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on a reset button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "[object PointerEvent] click b:true c:true [clickMe] [1]")
    public void buttonClick() throws Exception {
        final String firstSnippet = "       <input type='button' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for clicking on an anchor.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent] click b:true c:true [clickMe] [1]",
            IE = "[object PointerEvent] click b:true c:true [clickMe] [1]")
    public void anchorClick() throws Exception {
        final String firstSnippet = "       <a href='#' name='clickMe' id='clickMe' size='2'\n";
        final String secondSnippet = ">anchor</a>\n";

        testClickEvents(firstSnippet, secondSnippet);
    }

    private void testClickEvents(final String firstSnippet, final String secondSnippet) throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "  <script type='text/javascript'>\n"
                + "  <!--\n"
                + "    function dumpEvent(event) {\n"
                + "      var msg = event;\n"
                + "      msg = msg + ' ' + event.type;\n"
                + "      msg = msg + ' b:' + event.bubbles;\n"
                + "      msg = msg + ' c:' + event.cancelable;\n"
                + "\n"
                + "      // target\n"
                + "      var eTarget;\n"
                + "      if (event.target) {\n"
                + "        eTarget = event.target;\n"
                + "      } else if (event.srcElement) {\n"
                + "        eTarget = event.srcElement;\n"
                + "      }\n"
                + "      // defeat Safari bug\n"
                + "      if (eTarget.nodeType == 3) {\n"
                + "        eTarget = eTarget.parentNode;\n"
                + "      }\n"
                + "\n"
                + "      if (eTarget.name) {\n"
                + "        msg = msg + ' [' + eTarget.name + ']';\n"
                + "      } else {\n"
                + "        msg = msg + ' [' + eTarget.id + ']';\n"
                + "      }\n"
                + "\n"
                + "      // key code\n"
                + "      var eCode;\n"
                + "      if (event.keyCode) {\n"
                + "        eCode = event.keyCode;\n"
                + "      } else if (event.which) {\n"
                + "        eCode = event.which;\n"
                + "      } else if (event.button) {\n"
                + "        eCode = event.button;\n"
                + "      }\n"
                + "      if (eCode) {\n"
                + "        var char = String.fromCharCode(eCode);\n"
                + "        msg = msg + ' [' + eCode + ']';\n"
                + "      } else {\n"
                + "        msg = msg + ' [-]';\n"
                + "      }\n"
                + "\n"
                + "      alert(msg);\n"
                + "    }\n"
                + "  //-->\n"
                + "  </script>\n"
                + "</head>\n"
                + "    <form id='form' name='form' action='#'>\n"
                + "        <input type='text' id='start' name='startText'/>\n"
                + "\n"
                + firstSnippet
                + "           onclick='dumpEvent(event);'\n"
//                + "           ondblclick='dumpEvent(event);'\n"
//                + "           oncontextmenu='dumpEvent(event);'\n"
//                + "           onfocus='dumpEvent(event);'\n"
//                + "           onblur='dumpEvent(event);'\n"
//                + "           onmousedown = 'dumpEvent(event);'\n"
//                + "           onmouseup = 'dumpEvent(event);'\n"
//                + "           onkeydown = 'dumpEvent(event);'\n"
//                + "           onkeyup = 'dumpEvent(event);'\n"
//                + "           onkeypress = 'dumpEvent(event);'\n"
//                + "           onselect = 'dumpEvent(event);'\n"
                + "           onchange = 'dumpEvent(event);'"
                + secondSnippet
                + "   </form>\n"
                + "</body>\n"
                + "</html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test event order for typing into an entry field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[object KeyboardEvent] keydown b:true c:true [typeHere] [65]",
                "[object KeyboardEvent] keypress b:true c:true [typeHere] [97]",
                "[object KeyboardEvent] keyup b:true c:true [typeHere] [65]" })
    public void inputTextType() throws Exception {
        final String firstSnippet = "       <input type='text' id='typeHere'\n";
        final String secondSnippet = "/>\n";

        testTypeEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for typing into an password field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[object KeyboardEvent] keydown b:true c:true [typeHere] [65]",
                "[object KeyboardEvent] keypress b:true c:true [typeHere] [97]",
                "[object KeyboardEvent] keyup b:true c:true [typeHere] [65]" })
    public void inputPasswordType() throws Exception {
        final String firstSnippet = "       <input type='password' id='typeHere'\n";
        final String secondSnippet = "/>\n";

        testTypeEvents(firstSnippet, secondSnippet);
    }

    /**
     * Test event order for typing into an password field.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[object KeyboardEvent] keydown b:true c:true [typeHere] [65]",
                "[object KeyboardEvent] keypress b:true c:true [typeHere] [97]",
                "[object KeyboardEvent] keyup b:true c:true [typeHere] [65]" })
    public void textAreaType() throws Exception {
        final String firstSnippet = "       <textarea id='typeHere' rows='4' cols='2'\n";
        final String secondSnippet = "></textarea >\n";

        testTypeEvents(firstSnippet, secondSnippet);
    }

    private void testTypeEvents(final String firstSnippet, final String secondSnippet) throws Exception {
        final String html =
                "<html>\n"
                + "<head>\n"
                + "  <script type='text/javascript'>\n"
                + "  <!--\n"
                + "    function dumpEvent(event) {\n"
                + "      var msg = event;\n"
                + "      msg = msg + ' ' + event.type;\n"
                + "      msg = msg + ' b:' + event.bubbles;\n"
                + "      msg = msg + ' c:' + event.cancelable;\n"
                + "\n"
                + "      // target\n"
                + "      var eTarget;\n"
                + "      if (event.target) {\n"
                + "        eTarget = event.target;\n"
                + "      } else if (event.srcElement) {\n"
                + "        eTarget = event.srcElement;\n"
                + "      }\n"
                + "      // defeat Safari bug\n"
                + "      if (eTarget.nodeType == 3) {\n"
                + "        eTarget = eTarget.parentNode;\n"
                + "      }\n"
                + "\n"
                + "      if (eTarget.name) {\n"
                + "        msg = msg + ' [' + eTarget.name + ']';\n"
                + "      } else {\n"
                + "        msg = msg + ' [' + eTarget.id + ']';\n"
                + "      }\n"
                + "\n"
                + "      // key code\n"
                + "      var eCode;\n"
                + "      if (event.keyCode) {\n"
                + "        eCode = event.keyCode;\n"
                + "      } else if (event.which) {\n"
                + "        eCode = event.which;\n"
                + "      } else if (event.button) {\n"
                + "        eCode = event.button;\n"
                + "      }\n"
                + "      if (eCode) {\n"
                + "        var char = String.fromCharCode(eCode);\n"
                + "        msg = msg + ' [' + eCode + ']';\n"
                + "      } else {\n"
                + "        msg = msg + ' [-]';\n"
                + "      }\n"
                + "\n"
                + "      alert(msg);\n"
                + "    }\n"
                + "  //-->\n"
                + "  </script>\n"
                + "</head>\n"
                + "    <form id='form' name='form' action='#'>\n"
                + "        <input type='text' id='start' name='startText'/>\n"
                + "\n"
                + firstSnippet
                + "           onclick='dumpEvent(event);'\n"
//                + "           ondblclick='dumpEvent(event);'\n"
//                + "           oncontextmenu='dumpEvent(event);'\n"
//                + "           onfocus='dumpEvent(event);'\n"
//                + "           onblur='dumpEvent(event);'\n"
//                + "           onmousedown = 'dumpEvent(event);'\n"
//                + "           onmouseup = 'dumpEvent(event);'\n"
                + "           onkeydown = 'dumpEvent(event);'\n"
                + "           onkeyup = 'dumpEvent(event);'\n"
                + "           onkeypress = 'dumpEvent(event);'\n"
//                + "           onselect = 'dumpEvent(event);'\n"
                + "           onchange = 'dumpEvent(event);'"
                + secondSnippet
                + "   </form>\n"
                + "</body>\n"
                + "</html>\n";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("typeHere")).sendKeys("a");

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Tests that event fires on key press.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "pass", "fail:66", "fail:undefined" })
    public void eventOnKeyDown() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "  <button type='button' id='clickId'>Click Me</button>\n"
            + "  <script>\n"
            + "    function handler(_e) {\n"
            + "      var e = _e ? _e : window.event;\n"
            + "      if (e.keyCode == 65)\n"
            + "        alert('pass');\n"
            + "      else\n"
            + "        alert('fail:' + e.keyCode);\n"
            + "    }\n"
            + "    document.getElementById('clickId').onkeydown = handler;\n"
            + "    document.getElementById('clickId').onclick = handler;\n"
            + "  </script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("clickId"));
        element.sendKeys("a");
        element.sendKeys("b");
        element.click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Verifies that in IE, the <tt>shiftKey</tt>, <tt>ctrlKey</tt> and <tt>altKey</tt>
     * event attributes are defined for all events, but <tt>metaKey</tt> is not defined
     * for any events.<br/>
     * Verifies that in FF, the <tt>shiftKey</tt>, <tt>ctrlKey</tt>, <tt>altKey</tt> and
     * <tt>metaKey</tt> attributes are defined for mouse events only.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"object", "undefined", "undefined", "undefined", "undefined",
            "object", "false", "false", "false", "false" })
    public void testKeys() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.shiftKey);\n"
            + "        alert(e.ctrlKey);\n"
            + "        alert(e.altKey);\n"
            + "        alert(e.metaKey);\n"
            + "    }\n"
            + "</script>\n"
            + "<div id='div' onclick='test(event)'>abc</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("div"));
        element.click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault() throws Exception {
        final String html =
            "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function block(e) {\n"
            + "  if (e && e.preventDefault)\n"
            + "    e.preventDefault();\n"
            + "  else\n"
            + "    return false;\n"
            + "}\n"
            + "\n"
            + "function test() {\n"
            + "  document.getElementById('myForm').onsubmit = block;\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form id='myForm' action='doesnt_exist.html'>\n"
            + "  <input type='submit' id='mySubmit' value='Continue'></p>\n"
            + "</form>"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.id("mySubmit"));
        assertEquals(getDefaultUrl().toExternalForm(), driver.getCurrentUrl());
    }

    /**
     * Test Mozilla DOMContentLoaded event.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "DOMContentLoaded type=DOMContentLoaded", "onLoad" })
    public void testDOMContentLoaded() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>DOMContentLoaded</title>\n"
            + "<script>\n"
            + "  document.addEventListener('DOMContentLoaded', onDCL, false);\n"
            + "  function onDCL(e) {\n"
            + "    alert('DOMContentLoaded type=' + e.type);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onLoad\")'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "not canceled", "true", "canceled", "true" })
    public void testPreventDefault() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>preventDefault - copied from mozilla.org</title>\n"
            + "<script>\n"
            + "  function preventDef(event) {\n"
            + "    event.preventDefault();\n"
            + "  }\n"

            + "  function addHandler() {\n"
            + "    document.getElementById('checkbox').addEventListener('click', preventDef, false);\n"
            + "  }\n"

            + "  function simulateClick() {\n"
            + "    var evt = document.createEvent('MouseEvents');\n"
            + "    evt.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0,"
                        + " false, false, false, false, 0, null);\n"
            + "    var cb = document.getElementById('checkbox');\n"
            + "    var canceled = !cb.dispatchEvent(evt);\n"
            + "    if(canceled) {\n"
            + "      // A handler called preventDefault\n"
            + "      alert('canceled');\n"
            + "    } else {\n"
            + "      // None of the handlers called preventDefault\n"
            + "      alert('not canceled');\n"
            + "    }\n"
            + "  }\n"

            + "  function test() {\n"
            + "    if (document.createEvent) {\n"
            + "      alert(document.getElementById('checkbox').checked);\n"
            + "      simulateClick();\n"
            + "      alert(document.getElementById('checkbox').checked);\n"
            + "      addHandler();\n"
            + "      simulateClick();\n"
            + "      alert(document.getElementById('checkbox').checked);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='checkbox' id='checkbox'/><label for='checkbox'>Checkbox</label>"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test event transmission to event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "SPAN" },
            FF = { "false", "true", "SPAN" })
    public void eventTransmission() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <span id='clickMe'>foo</span>\n"
            + "  <script>\n"
            + "    function handler(e) {\n"
            + "      alert(e == null);\n"
            + "      alert(window.event == null);\n"
            + "      var theEvent = (e != null) ? e : window.event;\n"
            + "      var target = theEvent.target ? theEvent.target : theEvent.srcElement;\n"
            + "      alert(target.tagName);\n"
            + "    }\n"
            + "    document.getElementById('clickMe').onclick = handler;\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "capturing", "at target", "bubbling" })
    public void eventPhase() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script>\n"
            + "  function init() {\n"
            + "    var form = document.forms[0];\n"
            + "    form.addEventListener('click', alertPhase, true);\n"
            + "    form.addEventListener('click', alertPhase, false);\n"
            + "  }\n"

            + "  function alertPhase(e) {\n"
            + "    switch (e.eventPhase) {\n"
            + "      case 1: alert('capturing'); break;\n"
            + "      case 2: alert('at target'); break;\n"
            + "      case 3: alert('bubbling'); break;\n"
            + "      default: alert('unknown');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "  <form>\n"
            + "    <input type='button' onclick='alertPhase(event)' id='b'>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("b")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test for event capturing and bubbling in FF.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "window capturing", "div capturing", "span capturing",
                "span bubbling", "div", "div bubbling", "window bubbling" })
    public void eventCapturingAndBubbling() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function t(_s) {\n"
            + "    return function() { alert(_s) };\n"
            + "  }\n"

            + "  function init() {\n"
            + "    window.addEventListener('click', t('window capturing'), true);\n"
            + "    window.addEventListener('click', t('window bubbling'), false);\n"
            + "    var oDiv = document.getElementById('theDiv');\n"
            + "    oDiv.addEventListener('click', t('div capturing'), true);\n"
            + "    oDiv.addEventListener('click', t('div bubbling'), false);\n"
            + "    var oSpan = document.getElementById('theSpan');\n"
            + "    oSpan.addEventListener('click', t('span capturing'), true);\n"
            + "    oSpan.addEventListener('click', t('span bubbling'), false);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='init()'>\n"
            + "  <div onclick=\"alert('div')\" id='theDiv'>\n"
            + "    <span id='theSpan'>blabla</span>\n"
            + "  </div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("theSpan")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test for event capturing and bubbling.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "window capturing", "div capturing", "span capturing", "div", "window capturing, false, true" },
            CHROME = { "window capturing", "div capturing", "span capturing", "div", "window capturing, false, false" })
    @NotYetImplemented(CHROME)
    public void stopPropagation() throws Exception {
        stopPropagation("stopPropagation()");
    }

    /**
     * Test for event capturing and bubbling in FF.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "window capturing", "div capturing", "span capturing", "div", "window capturing, false, true" },
            CHROME = { "window capturing", "div capturing", "span capturing", "div", "window capturing, false, true",
                        "div capturing, true, true", "span capturing, true, true" },
            IE = { "window capturing", "div capturing", "span capturing", "div", "window capturing, false, false",
                        "div capturing, false, false", "span capturing, false, true" })
    @NotYetImplemented({ CHROME, IE })
    public void stopPropagationCancelBubble() throws Exception {
        stopPropagation("cancelBubble=true");
    }

    private void stopPropagation(final String cancelMethod) throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  var counter = 0;\n"
            + "  function t(_s) {\n"
            + "    return function(e) {\n"
            + "      alert(_s); counter++;\n"
            + "      if (counter >= 4) {\n"
            + "        alert(e.cancelBubble);\n"
            + "        e." + cancelMethod + ";\n"
            + "        alert(e.cancelBubble);\n"
            + "      };\n"
            + "    };\n"
            + "  }\n"
            + "  function init() {\n"
            + "    window.addEventListener('click', t('window capturing'), true);\n"
            + "    var oDiv = document.getElementById('theDiv');\n"
            + "    oDiv.addEventListener('click', t('div capturing'), true);\n"
            + "    var oSpan = document.getElementById('theSpan');\n"
            + "    oSpan.addEventListener('click', t('span capturing'), true);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='init()'>\n"
            + "  <div onclick=\"alert('div')\" id='theDiv'>\n"
            + "    <span id='theSpan'>blabla</span>\n"
            + "  </div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("theSpan")).click();
        driver.findElement(By.id("theSpan")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "w", "w 2", "d", "d 2", "s", "s 2", "w", "w 2" })
    public void stopPropagation_WithMultipleEventHandlers() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  var counter = 0;\n"
            + "  function t(_s) {\n"
            + "    return function(e) { alert(_s); counter++; if (counter >= 5) e.stopPropagation(); };\n"
            + "  }\n"
            + "  function init() {\n"
            + "    window.addEventListener('click', t('w'), true);\n"
            + "    window.addEventListener('click', t('w 2'), true);\n"
            + "    var oDiv = document.getElementById('theDiv');\n"
            + "    oDiv.addEventListener('click', t('d'), true);\n"
            + "    oDiv.addEventListener('click', t('d 2'), true);\n"
            + "    var oSpan = document.getElementById('theSpan');\n"
            + "    oSpan.addEventListener('click', t('s'), true);\n"
            + "    oSpan.addEventListener('click', t('s 2'), true);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='init()'>\n"
            + "<div id='theDiv'>\n"
            + "<span id='theSpan'>blabla</span>\n"
            + "</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("theSpan")).click();
        driver.findElement(By.id("theSpan")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }
}