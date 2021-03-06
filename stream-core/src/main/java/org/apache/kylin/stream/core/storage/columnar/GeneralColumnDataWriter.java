/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.apache.kylin.stream.core.storage.columnar;

import java.io.DataOutputStream;
import java.io.IOException;

public class GeneralColumnDataWriter implements ColumnDataWriter{
    private DataOutputStream dataOutput;
    private int totalBytes;
    private int[] valueOffsets;
    private int valueNum;

    public GeneralColumnDataWriter(int numOfVals, DataOutputStream dataOutput) {
        this.dataOutput = dataOutput;
        this.valueOffsets = new int[numOfVals];
    }

    @Override
    public void write(byte[] value) throws IOException {
        write(value, 0, value.length);
    }

    public void write(byte[] value, int offset, int len) throws IOException {
        dataOutput.writeInt(len);
        dataOutput.write(value, offset, len);
        totalBytes += len + 4;
        valueOffsets[valueNum] = totalBytes;
        valueNum++;
    }

    private void writeIndex() throws IOException {
        for (int i = 0; i < valueOffsets.length; i++) {
            dataOutput.writeInt(valueOffsets[i]);
        }
        dataOutput.writeInt(valueOffsets.length);
    }

    @Override
    public void flush() throws IOException {
        writeIndex();
        dataOutput.flush();
    }
}
