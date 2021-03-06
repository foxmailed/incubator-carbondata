/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.carbondata.core.carbon.datastore.block;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.carbondata.core.cache.Cacheable;
import org.apache.carbondata.core.carbon.datastore.DataRefNode;
import org.apache.carbondata.core.carbon.metadata.blocklet.DataFileFooter;

public abstract class AbstractIndex implements Cacheable {

  /**
   * vo class which will hold the RS information of the block
   */
  protected SegmentProperties segmentProperties;

  /**
   * data block
   */
  protected DataRefNode dataRefNode;

  /**
   * total number of row present in the block
   */
  protected long totalNumberOfRows;

  /**
   * atomic integer to maintain the access count for a column access
   */
  protected AtomicInteger accessCount = new AtomicInteger();

  /**
   * Table block meta size.
   */
  protected long memorySize;

  /**
   * @return the segmentProperties
   */
  public SegmentProperties getSegmentProperties() {
    return segmentProperties;
  }

  /**
   * @return the dataBlock
   */
  public DataRefNode getDataRefNode() {
    return dataRefNode;
  }

  @Override public long getFileTimeStamp() {
    return 0;
  }

  /**
   * Below method will be used to load the data block
   *
   * @param footerList footer list
   */
  public abstract void buildIndex(List<DataFileFooter> footerList);

  /**
   * the method returns the access count
   *
   * @return
   */
  @Override public int getAccessCount() {
    return accessCount.get();
  }

  /**
   * The method returns table block size
   *
   * @return
   */
  @Override public long getMemorySize() {
    return this.memorySize;
  }

  /**
   * The method is used to set the access count
   */
  public void incrementAccessCount() {
    accessCount.incrementAndGet();
  }

  /**
   * This method will release the objects and set default value for primitive types
   */
  public void clear() {
    decrementAccessCount();
  }

  /**
   * This method will decrement the access count for a column by 1
   * whenever a column usage is complete
   */
  private void decrementAccessCount() {
    if (accessCount.get() > 0) {
      accessCount.decrementAndGet();
    }
  }

  /**
   * the method is used to set the memory size of the b-tree
   * @param memorySize
   */
  public void setMemorySize(long memorySize) {
    this.memorySize = memorySize;
  }
}
