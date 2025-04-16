package com.example.todolist.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TaskWithTag {
        @Embedded
        public Task task;
        @Relation(parentColumn = "tag_id", entityColumn = "uid")
        public Tag tag;
    }

