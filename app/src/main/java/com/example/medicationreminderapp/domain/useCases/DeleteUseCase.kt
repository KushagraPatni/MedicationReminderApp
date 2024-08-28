package com.example.medicationreminderapp.domain.useCases

import com.example.medicationreminderapp.domain.model.Reminder
import com.example.medicationreminderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder)=reminderRepository.delete(reminder)
}