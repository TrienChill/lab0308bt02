package com.example.lab0308bt02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.lab0308bt02.ui.theme.Lab0308bt02Theme

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab0308bt02.ui.theme.Lab0308bt02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab0308bt02Theme {
                TaskManagerPreview()
            }
        }
    }
}

@Composable
fun TaskManagerScreen() {
    var taskList by remember { mutableStateOf(mutableListOf<Task>()) }
    var filter by remember { mutableStateOf(TaskFilter.ALL) }
    var newTaskName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Text(
                text = "Quản lý công việc",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Text field to enter new task
                BasicTextField(
                    value = newTaskName,
                    onValueChange = { newTaskName = it },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (newTaskName.isEmpty()) {
                            Text(text = "Nhập tên công việc mới", style = MaterialTheme.typography.bodyLarge)
                        }
                        innerTextField()
                    }
                )

                // Button to add new task
                Button(
                    onClick = {
                        if (newTaskName.isNotEmpty()) {
                            taskList.add(Task(newTaskName, false))
                            newTaskName = ""
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("Thêm công việc")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Radio group for filtering tasks
                Row(modifier = Modifier.padding(16.dp)) {
                    RadioButton(selected = filter == TaskFilter.ALL, onClick = { filter = TaskFilter.ALL })
                    Text(text = "Tất cả", modifier = Modifier.padding(start = 8.dp))

                    RadioButton(selected = filter == TaskFilter.COMPLETED, onClick = { filter = TaskFilter.COMPLETED })
                    Text(text = "Hoàn thành", modifier = Modifier.padding(start = 8.dp))

                    RadioButton(selected = filter == TaskFilter.INCOMPLETE, onClick = { filter = TaskFilter.INCOMPLETE })
                    Text(text = "No", modifier = Modifier.padding(start = 1.dp))
                }

                // List of tasks
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val filteredTasks = when (filter) {
                        TaskFilter.ALL -> taskList
                        TaskFilter.COMPLETED -> taskList.filter { it.isCompleted }
                        TaskFilter.INCOMPLETE -> taskList.filter { !it.isCompleted }
                    }

                    items(filteredTasks.size) { index ->
                        TaskItem(
                            task = filteredTasks[index],
                            onTaskStatusChanged = { isChecked -> taskList[index].isCompleted = isChecked },
                            onDeleteTask = { taskList.removeAt(index) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun TaskItem(
    task: Task,
    onTaskStatusChanged: (Boolean) -> Unit,
    onDeleteTask: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = task.name, style = MaterialTheme.typography.bodyLarge)

        Row {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskStatusChanged(it) }
            )

            Button(onClick = onDeleteTask) {
                Text("Xóa")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    val isDarkMode = remember { mutableStateOf(false) }
    Lab0308bt02Theme(darkTheme = isDarkMode.value){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(onClick = {isDarkMode.value = !isDarkMode.value}) {
                Text(text = if (isDarkMode.value) "Switch to Lingt Mode" else "Switch to Dark Mode")
            }
            TaskManagerScreen()
        }
    }

}
