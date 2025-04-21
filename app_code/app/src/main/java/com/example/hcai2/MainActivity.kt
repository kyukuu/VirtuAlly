package com.example.hcai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import VideoPlayer
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material.icons.sharp.Home  // Sharp style
import androidx.compose.material.icons.twotone.Home  // Two-tone style
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtuAllyApp()
        }
    }
}



//@Composable
//fun WelcomeScreen(navController: NavController) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                text = "Virtu-Ally",
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { navController.navigate("signup") },
//                modifier = Modifier.width(200.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
//            ) {
//                Text(
//                    text = "get started ->",
//                    color = Color.Black
//                )
//            }
//
//            Spacer(modifier = Modifier.weight(0.5f))
//
//            // Character image
//            Image(
//                painter = painterResource(id = R.drawable.character),
//                contentDescription = "Character",
//                modifier = Modifier
//                    .size(240.dp)
//                    .padding(bottom = 16.dp)
//                    .fillMaxWidth()
//
//            )
//        }
//
//        // Wave background at bottom
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(80.dp)
//                .align(Alignment.BottomCenter)
//                .background(Color.LightGray.copy(alpha = 0.3f))
//        )
//    }
//}

@Composable
fun AudioScreen(navController: NavController) {

    var isRecording by remember { mutableStateOf(false) }
    var currentPromptIndex by remember { mutableStateOf(0) }
    var recordedAudios by remember { mutableStateOf(listOf<String>()) }


    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Reading Practice")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }

    // When recording state changes, update the activity
    LaunchedEffect(isRecording) {
        if (isRecording) {
            ActivityRepository.startActivity("Voice Recording")
        }
    }

    val readingPrompts = listOf(
        "Tom had a small puppy. One day, the puppy ran out of the house. Tom looked around but couldn’t find him. He sat on the steps and began to cry.",
        "Sara had a red apple. Her friend Mia didn’t have any snack. Sara smiled and gave Mia half of her apple. Mia said, “Thank you!”",
        "Ben climbed up the slide. He was a little scared at the top. His sister waved and said, “You can do it!” Ben smiled and slid down fast.",
        "Lily wanted to play outside, but the rain started. She watched the rain from the window and sighed. Her dad brought her crayons, and they drew pictures together."
    )

    val questionPrompts = listOf(
        listOf(
            "Where did Tom's puppy go?",
            "How did Tom feel when he couldn't find his puppy?",
            "What did Tom do when he was sad?"
        ),
        listOf(
            "What color was Sara's apple?",
            "Who didn't have a snack?",
            "Was Sara kind to her friend?"
        ),
        listOf(
            "Where did Ben climb?",
            "How did Ben feel at the top of the slide?",
            "Who helped Ben feel brave?"
        ),
        listOf(
            "Why couldn't Lily play outside?",
            "What did Lily do when it rained?",
            "Who helped Lily find something else to do?"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Text(
            text = "Reading Practice",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Text prompt card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = LightMint)
        ) {
            Text(
                text = readingPrompts[currentPromptIndex],
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
//                textAlign = TextAlign.Center,
                textAlign = TextAlign.Justify,
                lineHeight = 28.sp,
                modifier = Modifier.padding(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(32.dp))
                // Questions section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QuestionAnswer,
                        contentDescription = "Questions",
                        tint = DarkGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Questions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkGreen
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = LightMint)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        questionPrompts[currentPromptIndex].forEachIndexed { index, question ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { /* Optional: handle question selection */ },
                                shape = RoundedCornerShape(8.dp),
//                                border = BorderStroke(1.dp, Color(0xFFD7ECD8)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(DarkGreen, CircleShape)
                                            .border(1.dp, Color(0xFFD7ECD8), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = question,
                                        textAlign = TextAlign.Justify,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                            if (index < questionPrompts[currentPromptIndex].size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

        Spacer(modifier = Modifier.height(16.dp))
//        Spacer(modifier = Modifier.height(32.dp))

        // Recording status
        AnimatedVisibility(visible = isRecording) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recording...",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous button
            IconButton(
                onClick = {
                    if (currentPromptIndex > 0) currentPromptIndex--
                },
                enabled = currentPromptIndex > 0
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentPromptIndex > 0) Color.Gray else Color.LightGray
                )
            }

            // Play/Pause button
            IconButton(
                onClick = { isRecording = !isRecording },
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        if (isRecording) Color.Red else Color.Gray,
                        CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Rounded.Mic,
                    contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Next button
            IconButton(
                onClick = {
                    if (currentPromptIndex < readingPrompts.size - 1) currentPromptIndex++
                },
                enabled = currentPromptIndex < readingPrompts.size - 1
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentPromptIndex < readingPrompts.size - 1) Color.Gray else Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Tap the microphone button to start recording your reading",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom navigation placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
    }

    // Bottom Navigation
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavigationBar(navController)
    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFbcf6cf))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Virtu-Ally",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("signup") },
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A4C40)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "get started ->",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // The image container with full width background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .background(Color(0xFFF2F2F2)) // Light gray background like in your image
            ) {
                Image(
                    painter = painterResource(id = R.drawable.character),
                    contentDescription = "Character",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), // Add some padding on top if needed
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sign up with your email",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Gmail",
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.Gray
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Password",
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.Gray
                )
            }
//            trailingIcon = {
//                Icon(
//                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                    contentDescription = "Toggle Password Visibility",
//                    modifier = Modifier.clickable { passwordVisible = !passwordVisible },
//                    tint = Color.Gray
//                )
//            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Confirm password",
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("••••••••") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirm Password Icon",
                    tint = Color.Gray
                )
            }
//            trailingIcon = {
//                Icon(
//                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                    contentDescription = "Toggle Password Visibility",
//                    modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible },
//                    tint = Color.Gray
//                )
//            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("data_privacy") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A4C40))
        ) {
            Text("Sign up")
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome back",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Continue with your email",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Gmail",
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.Gray
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Password",
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.Gray
                )
            }
//            },
//            trailingIcon = {
//                Icon(
//                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                    contentDescription = "Toggle Password Visibility",
//                    modifier = Modifier.clickable { passwordVisible = !passwordVisible },
//                    tint = Color.Gray
//                )
//            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberPassword,
                onCheckedChange = { rememberPassword = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Gray,
                    uncheckedColor = Color.Gray
                )
            )

            Text(
                text = "Remember password",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Forgot password?",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { /* Action */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("select_mode") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Log in")
        }
    }
}

@Composable
fun DataPrivacyScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Data & Privacy",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TL;DR Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = LightMint)
        ) {
            Text(
                text = "By clicking on proceed you agree to our terms and conditions and let virtu-ally capture your child's image and speech data and use it for evalution, we do not store this data.",
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Justify,
//                textAlign = TextAlign.Start,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Policy in scrollable container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "To provide early detection of potential signs of Virtual Autism, Virtu-Ally analyzes facial expressions and speech patterns during screen interactions. All data collected is securely stored, encrypted, and used exclusively to improve detection accuracy and provide insights to parents. We do not share your child's data with third parties. Participation is voluntary, and you may opt out or delete your data at any time. By continuing, you confirm that you are the child's legal guardian and consent to the use of anonymized data for analysis, improvement of our AI model, and personalized reporting. For more detailed information on how we handle data, visit our Data & Privacy FAQs.\n\n" +
                        "To provide early detection of potential signs of Virtual Autism, Virtu-Ally analyzes facial expressions and speech patterns during screen interactions. All data collected is securely stored, encrypted, and used exclusively to improve detection accuracy and provide insights to parents. We do not share your child's data with third parties. Participation is voluntary, and you may opt out or delete your data at any time. By continuing, you confirm that you are the child's legal guardian and consent to the use of anonymized data for analysis, improvement of our AI model, and personalized reporting. For more detailed information on how we handle data, visit our Data & Privacy FAQs.\n\n" +
                        "To provide early detection of potential signs of Virtual Autism, Virtu-Ally analyzes facial expressions and speech patterns during screen interactions. All data collected is securely stored, encrypted, and used exclusively to improve detection accuracy and provide insights to parents. We do not share your child's data with third parties. Participation is voluntary, and you may opt out or delete your data at any time. By continuing, you confirm that you are the child's legal guardian and consent to the use of anonymized data for analysis, improvement of our AI model, and personalized reporting. For more detailed information on how we handle data, visit our Data & Privacy FAQs.\n\n" +
                        "To provide early detection of potential signs of Virtual Autism, Virtu-Ally analyzes facial expressions and speech patterns during screen interactions. All data collected is securely stored, encrypted, and used exclusively to improve detection accuracy and provide insights to parents. We do not share your child's data with third parties. Participation is voluntary, and you may opt out or delete your data at any time. By continuing, you confirm that you are the child's legal guardian and consent to the use of anonymized data for analysis, improvement of our AI model, and personalized reporting. For more detailed information on how we handle data, visit our Data & Privacy FAQs.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Button(
            onClick = { navController.navigate("select_mode") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A4C40))
        ) {
            Text("Continue")
        }

        TextButton(
            onClick = { /* Action */ },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Learn More",
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// App color scheme
val DarkGreen = Color(0xFF3A4C40)
val LightMint = Color(0xFFa8f0c8)

@Composable
fun SelectModeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)  // White background to match Reading Practice screen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)  // Increased padding for more breathing room
        ) {
            // Header with title and parent mode button
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Select Mode",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Parent mode button - direct navigation without password
                Button(
                    onClick = { navController.navigate("user_dashboard") },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen // Light gray like in reading screen
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Parent Mode",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Parent Mode",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            // Child mode card - centered with minimal design
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(380.dp)
                        .height(500.dp)
                        .clickable { navController.navigate("choose_video") },
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = LightMint // Light gray background matching the card style
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Image container
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.child_icon),
                                contentDescription = "Child Mode",
                                modifier = Modifier.size(240.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Mode title and description
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(
                                text = "Child Mode",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Pick a learning game to play",
                                fontSize = 15.sp,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomNavigationBar(navController)
        }
    }
}

@Composable
fun VirtuAllyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("data_privacy") { DataPrivacyScreen(navController) }
        composable("select_mode") { SelectModeScreen(navController) }
        composable("choose_video") { ChooseVideoScreen(navController) }
        composable("video_player_1") { VideoPlayerScreen1(navController) }
        composable("video_player_2") { VideoPlayerScreen2(navController) }
        composable("video_player_3") { VideoPlayerScreen3(navController) }
        composable("video_player_4") { VideoPlayerScreen4(navController) }
        composable("video_player_5") { VideoPlayerScreen5(navController) }
        composable("user_dashboard") { UserDashboardScreen(navController) }
        composable("audio_screen") { AudioScreen(navController) }
    }
}

@Composable
fun ChooseVideoScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 56.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Choose a video",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                // Nursery Rhymes category
                Text(
                    text = "Nursery Rhymes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )

                VideoOptionCard(
                    title = "Cocomelon - Nursery Rhymes",
                    description = "Educational songs and nursery rhymes for children",
                    duration = "3:16",
                    imageId = R.drawable.character,
                    onClick = {
                        // Link: https://www.youtube.com/watch?v=e_04ZrNroTo
                        navController.navigate("video_player_1")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                VideoOptionCard(
                    title = "Baby Shark Dance",
                    description = "Sing and dance along with Baby Shark!",
                    duration = "2:16",
                    imageId = R.drawable.character,
                    onClick = {
                        // Link: https://www.youtube.com/watch?v=XqZsoesa55w
                        navController.navigate("video_player_2")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Disney Music category
                Text(
                    text = "Disney Songs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )

                VideoOptionCard(
                    title = "Disney Music - Let It Go",
                    description = "Popular Disney song from Frozen",
                    duration = "3:45",
                    imageId = R.drawable.character,
                    onClick = {
                        // Link: https://www.youtube.com/watch?v=YVVTZgwYwVo
                        navController.navigate("video_player_3")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                VideoOptionCard(
                    title = "Moana - How Far I'll Go",
                    description = "Inspirational Disney song about adventure",
                    duration = "3:22",
                    imageId = R.drawable.character,
                    onClick = {
                        // Link: https://www.youtube.com/watch?v=GibiNy4d4gc
                        navController.navigate("video_player_4")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                VideoOptionCard(
                    title = "Encanto - We Don't Talk About Bruno",
                    description = "Catchy and fun song from Encanto",
                    duration = "4:20",
                    imageId = R.drawable.character,
                    onClick = {
                        // Link: https://www.youtube.com/watch?v=G3lSONLLx70
                        navController.navigate("video_player_5")
                    }
                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Additional Videos category
//                Text(
//                    text = "Movie Song",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
//                )
//
//                VideoOptionCard(
//                    title = "Family Movie Fun",
//                    description = "Entertaining content for the whole family",
//                    duration = "5:12",
//                    imageId = R.drawable.character,
//                    onClick = {
//                        // Link: https://www.youtube.com/watch?v=iRUiM7kVBXA
//                        navController.navigate("video_player")
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                VideoOptionCard(
//                    title = "Musical Moments",
//                    description = "Enjoy these fun musical compositions",
//                    duration = "4:05",
//                    imageId = R.drawable.character,
//                    onClick = {
//                        // Link: https://www.youtube.com/watch?v=doUgXDKTmKI
//                        navController.navigate("video_player")
//                    }
//                )
            }
        }

        // Bottom navigation positioned at the bottom of the screen
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomNavigationBar(navController)
        }
    }
}
@Composable
fun VideoOptionCard(
    title: String,
    description: String,
    duration: String,
    imageId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, indication = null, interactionSource = remember { MutableInteractionSource() }),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail with play icon overlay
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE0E0E0))
            ) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Play icon overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x66000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = duration,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun VideoOption(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

//@Composable
//fun VideoPlayerScreen(navController: NavController) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(top = 40.dp)
//            .wrapContentWidth(Alignment.CenterHorizontally)
//    ) {
//        Text(
//            text = "Video Player",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(horizontal = 24.dp)
//                .wrapContentWidth(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(horizontal = 16.dp)
//        ) {
//            // Video player placeholder
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16f / 9f)
//                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
////                contentAlignment = Alignment.Center
//            ) {
//                VideoPlayer(
//                    modifier = Modifier.fillMaxWidth().height(340.dp),
//                    url = "https://www.youtube.com/watch?v=AD2nEllUMJw", // Automatically Detect the URL, Wether to Play YouTube Video or .mp4 e.g
//                    autoPlay = true,
//                    showControls = true
//                )
//            }
//            // Video controls at bottom
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(32.dp)
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Controls and overlay integration",
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                )
//            }
//        }
//
//        BottomNavigationBar(navController)
//    }
//}


//@Composable
//fun VideoPlayerScreen(navController: NavController) {
//    var isPlaying by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val photoJob = remember { mutableStateOf<Job?>(null) }
//
//    // Create directory for saving photos if it doesn't exist
//    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
//    if (!photoDir.exists()) {
//        photoDir.mkdirs()
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(top = 40.dp)
//            .wrapContentWidth(Alignment.CenterHorizontally)
//    ) {
//        Text(
//            text = "Video Player",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .padding(horizontal = 24.dp)
//                .wrapContentWidth(Alignment.CenterHorizontally)
//                .align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(1f)
//                .padding(horizontal = 16.dp)
//        ) {
//            // Video player container
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
////                    .aspectRatio(16f / 9f)
//                    .background(Color.LightGray, RoundedCornerShape(32.dp))
//            ) {
//                VideoPlayer(
//                    modifier = Modifier.fillMaxSize(),
//                    url = "https://www.youtube.com/watch?v=6c_toQ6rggg&list=PLsA-FvPg6Bo68lBe7qlZo_i1cVg8jcT8c&index=3&ab_channel=MoonbugKids-ExploringEmotionsandFeelings",
//                    autoPlay = false,
//                    showControls = true
//                )
//            }
//
////            // Video controls at bottom
////            Box(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .height(32.dp)
////                    .align(Alignment.BottomCenter)
////                    .padding(bottom = 16.dp),
////                contentAlignment = Alignment.Center
////            ) {
////                Text(
////                    text = if (isPlaying) "Recording frames..." else "Press play to start recording frames",
////                    fontSize = 12.sp,
////                    color = Color.Gray
////                )
////            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BottomNavigationBar(navController)
//    }
//}

@Composable
fun VideoPlayerScreen5(navController: NavController) {

    // This will be called when the composition is first created
    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Video Player")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }


    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Create directory for saving photos if it doesn't exist
    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
    if (!photoDir.exists()) {
        photoDir.mkdirs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            // Portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Video Player",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Video player container with padding, color and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    // Outer container with background color and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGreen, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // Video player
                        VideoPlayer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(32.dp)),
                            url = "https://www.youtube.com/watch?v=G3lSONLLx70",
                            autoPlay = false,
                            showControls = true
                        )

                        // Rotate to landscape button
                        Button(
                            onClick = {
                                // Request landscape orientation
                                (context as? Activity)?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x88000000),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Fullscreen")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom navigation in portrait mode only
                BottomNavigationBar(navController)
            }
        } else {
            // Landscape layout - full screen video only, no navigation bar
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = "https://www.youtube.com/watch?v=G3lSONLLx70",
                    autoPlay = false,
                    showControls = true
                )

                // Exit fullscreen button
                Button(
                    onClick = {
                        // Request portrait orientation
                        (context as? Activity)?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x88000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Exit Fullscreen")
                }
            }
        }
    }
}

@Composable
fun VideoPlayerScreen4(navController: NavController) {

    // This will be called when the composition is first created
    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Video Player")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }


    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Create directory for saving photos if it doesn't exist
    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
    if (!photoDir.exists()) {
        photoDir.mkdirs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            // Portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Video Player",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Video player container with padding, color and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    // Outer container with background color and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGreen, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // Video player
                        VideoPlayer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(32.dp)),
                            url = "https://www.youtube.com/watch?v=GibiNy4d4gc",
                            autoPlay = false,
                            showControls = true
                        )

                        // Rotate to landscape button
                        Button(
                            onClick = {
                                // Request landscape orientation
                                (context as? Activity)?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x88000000),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Fullscreen")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom navigation in portrait mode only
                BottomNavigationBar(navController)
            }
        } else {
            // Landscape layout - full screen video only, no navigation bar
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = "https://www.youtube.com/watch?v=GibiNy4d4gc",
                    autoPlay = false,
                    showControls = true
                )

                // Exit fullscreen button
                Button(
                    onClick = {
                        // Request portrait orientation
                        (context as? Activity)?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x88000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Exit Fullscreen")
                }
            }
        }
    }
}

@Composable
fun VideoPlayerScreen3(navController: NavController) {

    // This will be called when the composition is first created
    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Video Player")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }


    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Create directory for saving photos if it doesn't exist
    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
    if (!photoDir.exists()) {
        photoDir.mkdirs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            // Portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Video Player",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Video player container with padding, color and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    // Outer container with background color and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGreen, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // Video player
                        VideoPlayer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(32.dp)),
                            url = "https://www.youtube.com/watch?v=YVVTZgwYwVo",
                            autoPlay = false,
                            showControls = true
                        )

                        // Rotate to landscape button
                        Button(
                            onClick = {
                                // Request landscape orientation
                                (context as? Activity)?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x88000000),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Fullscreen")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom navigation in portrait mode only
                BottomNavigationBar(navController)
            }
        } else {
            // Landscape layout - full screen video only, no navigation bar
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = "https://www.youtube.com/watch?v=YVVTZgwYwVo",
                    autoPlay = false,
                    showControls = true
                )

                // Exit fullscreen button
                Button(
                    onClick = {
                        // Request portrait orientation
                        (context as? Activity)?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x88000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Exit Fullscreen")
                }
            }
        }
    }
}

@Composable
fun VideoPlayerScreen2(navController: NavController) {

    // This will be called when the composition is first created
    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Video Player")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }


    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Create directory for saving photos if it doesn't exist
    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
    if (!photoDir.exists()) {
        photoDir.mkdirs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            // Portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Video Player",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Video player container with padding, color and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    // Outer container with background color and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGreen, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // Video player
                        VideoPlayer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(32.dp)),
                            url = "https://www.youtube.com/watch?v=XqZsoesa55w",
                            autoPlay = false,
                            showControls = true
                        )

                        // Rotate to landscape button
                        Button(
                            onClick = {
                                // Request landscape orientation
                                (context as? Activity)?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x88000000),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Fullscreen")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom navigation in portrait mode only
                BottomNavigationBar(navController)
            }
        } else {
            // Landscape layout - full screen video only, no navigation bar
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = "https://www.youtube.com/watch?v=XqZsoesa55w",
                    autoPlay = false,
                    showControls = true
                )

                // Exit fullscreen button
                Button(
                    onClick = {
                        // Request portrait orientation
                        (context as? Activity)?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x88000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Exit Fullscreen")
                }
            }
        }
    }
}

@Composable
fun VideoPlayerScreen(navController: NavController) {

    // This will be called when the composition is first created
    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Video Player")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }


    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Create directory for saving photos if it doesn't exist
    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
    if (!photoDir.exists()) {
        photoDir.mkdirs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            // Portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Video Player",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Video player container with padding, color and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    // Outer container with background color and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGreen, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // Video player
                        VideoPlayer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(32.dp)),
                            url = "https://www.youtube.com/watch?v=6c_toQ6rggg&list=PLsA-FvPg6Bo68lBe7qlZo_i1cVg8jcT8c&index=3&ab_channel=MoonbugKids-ExploringEmotionsandFeelings",
                            autoPlay = false,
                            showControls = true
                        )

                        // Rotate to landscape button
                        Button(
                            onClick = {
                                // Request landscape orientation
                                (context as? Activity)?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x88000000),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Fullscreen")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom navigation in portrait mode only
                BottomNavigationBar(navController)
            }
        } else {
            // Landscape layout - full screen video only, no navigation bar
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = "https://www.youtube.com/watch?v=6c_toQ6rggg&list=PLsA-FvPg6Bo68lBe7qlZo_i1cVg8jcT8c&index=3&ab_channel=MoonbugKids-ExploringEmotionsandFeelings",
                    autoPlay = false,
                    showControls = true
                )

                // Exit fullscreen button
                Button(
                    onClick = {
                        // Request portrait orientation
                        (context as? Activity)?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x88000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Exit Fullscreen")
                }
            }
        }
    }
}

@Composable
fun VideoPlayerScreen1(navController: NavController) {

    // This will be called when the composition is first created
    LaunchedEffect(Unit) {
        ActivityRepository.startActivity("Video Player")
    }

    // Handle screen exit
    DisposableEffect(Unit) {
        onDispose {
            ActivityRepository.completeActiveSession()
        }
    }


    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Create directory for saving photos if it doesn't exist
    val photoDir = File(context.getExternalFilesDir(null), "captured_frames")
    if (!photoDir.exists()) {
        photoDir.mkdirs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            // Portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Video Player",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Video player container with padding, color and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    // Outer container with background color and rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGreen, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // Video player
                        VideoPlayer(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(32.dp)),
                            url = "https://www.youtube.com/watch?v=e_04ZrNroTo",
                            autoPlay = false,
                            showControls = true
                        )

                        // Rotate to landscape button
                        Button(
                            onClick = {
                                // Request landscape orientation
                                (context as? Activity)?.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x88000000),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Fullscreen")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom navigation in portrait mode only
                BottomNavigationBar(navController)
            }
        } else {
            // Landscape layout - full screen video only, no navigation bar
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = "https://www.youtube.com/watch?v=e_04ZrNroTo",
                    autoPlay = false,
                    showControls = true
                )

                // Exit fullscreen button
                Button(
                    onClick = {
                        // Request portrait orientation
                        (context as? Activity)?.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x88000000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Exit Fullscreen")
                }
            }
        }
    }
}

suspend fun captureFrame(photoDir: File) {
    withContext(Dispatchers.IO) {
        try {
            val timestamp = System.currentTimeMillis()
            val photoFile = File(photoDir, "frame_$timestamp.jpg")

            // TODO: Implement actual frame capture logic here
            // This will depend on your video player implementation
            // You might need to use VideoView or ExoPlayer's getBitmap() functionality
            // or implement a custom frame grabber

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

//@Composable
//fun UserDashboardScreen(navController: NavController) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(top = 40.dp)
//            .wrapContentWidth(Alignment.CenterHorizontally)
//    ) {
//        Text(
//            text = "User Dashboard",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(horizontal = 24.dp)
//                .wrapContentWidth(Alignment.CenterHorizontally)
//                .align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(horizontal = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Profile picture and info
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 32.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(64.dp)
//                        .background(Color.Gray, CircleShape)
//                        .padding(4.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "Profile",
//                        tint = Color.White,
//                        modifier = Modifier.size(32.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                Column {
//                    Text(
//                        text = "Name",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//
//                    Text(
//                        text = "+91 xxxxxxxxx",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//
//                    Text(
//                        text = "name@gmail.com",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            }
//
//            // Generate report button
//            Button(
//                onClick = { /* Action */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//            ) {
//                Text("Generate report")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Take a look at your child's report",
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
//        }
//
//        BottomNavigationBar(navController)
//    }
//}

@Composable
fun UserDashboardScreen(navController: NavController) {
    var showPasswordDialog by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var screenContent by remember { mutableStateOf(false) }

    // Show password dialog on first launch
    LaunchedEffect(Unit) {
        showPasswordDialog = true
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                // Navigate back to previous screen instead of causing a loop
                navController.navigateUp()
            },
            containerColor = Color.White,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Parent Access",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column {
                    Text(
                        text = "Please enter password to access dashboard",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                        },
                        label = { Text("Password", fontSize = 14.sp) },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordError,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    if (passwordError) {
                        Text(
                            "Incorrect password",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (password == "1234") {
                            showPasswordDialog = false
                            screenContent = true
                            password = ""
                        } else {
                            passwordError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirm", fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Navigate back to previous screen instead of causing a loop
                        navController.navigateUp()
                    }
                ) {
                    Text("Cancel", color = Color.Gray, fontSize = 14.sp)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "User Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Profile Section
        ProfileSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Child Details Section
        ChildDetailsSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Activity Section
        ActivitySection()

        Spacer(modifier = Modifier.height(24.dp))

        // Report Generation
        ReportSection()

        Spacer(modifier = Modifier.height(56.dp))

    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavigationBar(navController)
    }
}
@Composable
private fun ProfileSection() {
    var isEditing by remember { mutableStateOf(false) }
    var parentName by remember { mutableStateOf("Parent Name") }
    var phoneNumber by remember { mutableStateOf("+91 xxxxxxxxxx") }
    var email by remember { mutableStateOf("parent@gmail.com") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(DarkGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Display profile image if available, otherwise show default icon
                    if (profileImageUri != null) {
                        AsyncImage(
                            model = profileImageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Show camera/edit overlay when in edit mode
                    if (isEditing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x88000000))
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Change Picture",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = parentName,
                            onValueChange = { parentName = it },
                            label = { Text("Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = parentName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = phoneNumber,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                IconButton(onClick = { isEditing = !isEditing }) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (isEditing) "Save" else "Edit",
                        tint = Color.Gray
                    )
                }

            }
        }
    }
}
@Composable
private fun ChildDetailsSection() {
    var isEditing by remember { mutableStateOf(false) }
    var childName by remember { mutableStateOf("Child Name") }
    var childAge by remember { mutableStateOf("5") }
    var childImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            childImageUri = it
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Child Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Display child image if available, otherwise show default icon
                    if (childImageUri != null) {
                        AsyncImage(
                            model = childImageUri,
                            contentDescription = "Child Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Face,
                            contentDescription = "Child",
                            tint = DarkGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Show camera/edit overlay when in edit mode
                    if (isEditing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x88000000))
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Face,
                                contentDescription = "Change Picture",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = childName,
                            onValueChange = { childName = it },
                            label = { Text("Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = childAge,
                            onValueChange = { childAge = it },
                            label = { Text("Age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = childName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Age: $childAge years",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                IconButton(onClick = { isEditing = !isEditing }) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (isEditing) "Save" else "Edit",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

//@Composable
//private fun ProgressSection() {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                colors = CardDefaults.cardColors(
//                containerColor = Color.White
//                )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Progress Overview",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                ProgressItem(
//                    title = "Videos\nWatched",
//                    value = "12",
//                    color = Color.Unspecified
//                )
//                ProgressItem(
//                    title = "Time\nSpent",
//                    value = "3h 20m",
//                    color = Color.Unspecified
//                )
//                ProgressItem(
//                    title = "Weekly\nProgress",
//                    value = "85%",
//                    color = Color.Unspecified
//                )
//            }
//        }
//    }
//}


data class UserActivity(
    val title: String,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val duration: String = "",
    val id: String = UUID.randomUUID().toString()
)


object ActivityRepository {
    // Use mutableStateOf to make this observable in Compose
    private val _activities = mutableStateOf<List<UserActivity>>(listOf())
    val activities: State<List<UserActivity>> = _activities

    // Track currently active session
    private var activeSession: UserActivity? = null

    // Start recording a new activity
    fun startActivity(title: String) {
        // First complete any ongoing activity
        completeActiveSession()

        // Create and store new active session
        activeSession = UserActivity(
            title = title,
            startTime = System.currentTimeMillis()
        )
    }

    // Complete the active session
    fun completeActiveSession() {
        activeSession?.let { session ->
            val endTime = System.currentTimeMillis()
            val durationMs = endTime - session.startTime

            // Create completed activity with duration
            val completedActivity = session.copy(
                endTime = endTime,
                duration = formatDuration(durationMs)
            )

            // Add to activities list
            _activities.value = listOf(completedActivity) + _activities.value.take(4)

            // Clear active session
            activeSession = null
        }
    }

    // For backward compatibility
    fun recordActivity(title: String, duration: String) {
        val newActivity = UserActivity(
            title = title,
            duration = duration
        )
        _activities.value = listOf(newActivity) + _activities.value.take(4)
    }

    // Helper to format duration in milliseconds to readable string
    private fun formatDuration(durationMs: Long): String {
        val seconds = (durationMs / 1000) % 60
        val minutes = (durationMs / (1000 * 60)) % 60
        val hours = (durationMs / (1000 * 60 * 60))

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }

    // For initial demo data
    init {
        recordActivity("Emotions Video", "15 min")
        recordActivity("Communication Exercise", "20 min")
    }
}

@Composable
private fun ActivitySection() {
    // Observe activities from repository
    val activities by ActivityRepository.activities

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "View All",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable { /* Show all */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (activities.isEmpty()) {
                Text(
                    text = "No recent activity",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                activities.forEach { activity ->
                    ActivityItem(
                        title = activity.title,
                        time = formatTimestamp(activity.startTime),
                        duration = activity.duration
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ActivityItem(title: String, time: String, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Text(
            text = duration,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

// Helper function to format timestamps into readable strings
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60 * 60 * 1000 -> "Today ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp)}"
        diff < 24 * 60 * 60 * 1000 -> "Today ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp)}"
        diff < 48 * 60 * 60 * 1000 -> "Yesterday ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp)}"
        else -> "${diff / (24 * 60 * 60 * 1000)} days ago ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp)}"
    }
}

@Composable
private fun ReportSection() {
    Button(
        onClick = { /* Action */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(DarkGreen)
    ) {
        Text("Generate Detailed Report")
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    // Track the current route to highlight the active icon
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define navigation items
    val navigationItems = listOf(
        NavigationItem("select_mode", Icons.Rounded.Home, "Home"),
        NavigationItem("choose_video", Icons.Rounded.VideoLibrary, "Videos"),
        NavigationItem("audio_screen", Icons.Rounded.Mic, "Audio"),
        NavigationItem("user_dashboard", Icons.Rounded.Person, "Profile")
    )

    Surface(
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                val isSelected = currentRoute == item.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        navController.navigate(item.route)
                    }
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color.Black else Color.LightGray
                    )

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(2.dp)
                                .background(Color(0xFF616161))
                        )
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String
)