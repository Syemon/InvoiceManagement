package com.syemon.invoicemanagement;

import org.springframework.boot.SpringApplication;

public class TestInvoicemanagementApplication {

    public static void main(String[] args) {
        SpringApplication.from(InvoiceManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
