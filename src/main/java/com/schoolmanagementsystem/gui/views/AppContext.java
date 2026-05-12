package com.schoolmanagementsystem.gui.views;

import com.schoolmanagementsystem.services.AuthService;
import com.schoolmanagementsystem.services.CrudService;

// Holds the services the GUI passes around so every screen uses the same file-backed logic.
public record AppContext(
        AuthService authService,
        CrudService studentService,
        CrudService teacherService,
        CrudService feeService) {
}
