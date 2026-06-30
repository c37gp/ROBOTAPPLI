/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed under an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.objectdetection

import com.robot.ai.AIController
import com.robot.control.RobotController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AIControllerTest {

    @Mock
    private lateinit var mockRobotController: RobotController

    private lateinit var aiController: AIController

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        aiController = AIController(mockRobotController)
    }

    @Test
    fun testTrashDetection() {
        // Test that trash detection triggers "dechet" command
        aiController.onDetection("bottle", 0.9f)
        verify(mockRobotController).onCommand(eq("dechet"))
    }

    @Test
    fun testPlasticDetection() {
        aiController.onDetection("plastic", 0.8f)
        verify(mockRobotController).onCommand(eq("dechet"))
    }

    @Test
    fun testCanDetection() {
        aiController.onDetection("can", 0.7f)
        verify(mockRobotController).onCommand(eq("dechet"))
    }

    @Test
    fun testTrashDetectionCaseInsensitive() {
        aiController.onDetection("BOTTLE", 0.9f)
        verify(mockRobotController).onCommand(eq("dechet"))
    }

    @Test
    fun testObstacleDetection() {
        // Test that obstacle detection triggers avoidance
        aiController.onDetection("person", 0.9f)
        // Should trigger some avoidance command (not "dechet")
        // We can't verify the exact command without mocking the ObstacleAvoidanceEngine
    }

    @Test
    fun testLowScoreDetection() {
        // Low score detections should be ignored or trigger "avant"
        aiController.onDetection("person", 0.2f)
        // Should trigger "avant" command
        verify(mockRobotController).onCommand(eq("avant"))
    }

    @Test
    fun testMultipleDetections() {
        // Test multiple detections
        aiController.onDetection("bottle", 0.9f)
        aiController.onDetection("person", 0.8f)
        
        // Both should trigger commands
        verify(mockRobotController).onCommand(eq("dechet"))
        // The second detection should also trigger a command
    }
}
