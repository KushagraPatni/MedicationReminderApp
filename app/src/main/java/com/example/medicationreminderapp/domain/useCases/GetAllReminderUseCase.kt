package com.example.medicationreminderapp.domain.useCases

import com.example.medicationreminderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class GetAllReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
    operator fun invoke()= reminderRepository.getAllReminders()
}