/*
 * Copyright 2000-2022 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.uitest.ui.template;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import com.vaadin.flow.testutil.ChromeBrowserTest;
import com.vaadin.testbench.TestBenchElement;

public class EmptyListsIT extends ChromeBrowserTest {

    @Test
    public void emptyListsAreProperlyHandled() {
        open();

        TestBenchElement template = $("*").id("template");

        Assert.assertTrue(
                template.$("*").attributeContains("class", "item").exists());

        findElement(By.id("set-empty")).click();

        LogEntries logs = driver.manage().logs().get("browser");
        if (logs != null) {
            Optional<LogEntry> anyError = StreamSupport
                    .stream(logs.spliterator(), true)
                    .filter(entry -> entry.getLevel()
                            .intValue() > java.util.logging.Level.INFO
                                    .intValue())
                    .filter(entry -> !entry.getMessage()
                            .contains("favicon.ico"))
                    .filter(entry -> !entry.getMessage()
                            .contains("HTML Imports is deprecated"))
                    .filter(entry -> !entry.getMessage()
                            .contains("sockjs-node"))
                    .filter(entry -> !entry.getMessage()
                            .contains("[WDS] Disconnected!"))
                    // Web Socket error when trying to connect to Spring Dev
                    // Tools live-reload server.
                    .filter(entry -> !entry.getMessage()
                            .contains("WebSocket connection to 'ws://"))
                    .findAny();

            anyError.ifPresent(entry -> Assert.fail(entry.getMessage()));
        }
    }
}
