package com.example.medicationreminderapp.data.repository

import com.example.medicationreminderapp.data.local.ReminderDao
import com.example.medicationreminderapp.domain.model.Reminder
import com.example.medicationreminderapp.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class ReminderRepoImpl(private val reminderDao: ReminderDao):ReminderRepository {
    override suspend fun insert(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    override suspend fun update(reminder: Reminder) {
        reminderDao.update(reminder)
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

    override fun getAllReminders(): Flow<List<Reminder>> =reminderDao.getAllReminder()

}