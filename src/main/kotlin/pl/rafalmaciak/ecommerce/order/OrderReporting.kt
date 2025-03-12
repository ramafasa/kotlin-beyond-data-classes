package pl.rafalmaciak.ecommerce.order

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

internal object OrderReporting {


    /**
     * Creates Excel report for the given orders
     */
    fun createReportForOrders(orders: Set<Order>) {
        // Create a new workbook and sheet
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Orders Report")

        // Create header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Order ID")
        headerRow.createCell(1).setCellValue("User ID")
        headerRow.createCell(2).setCellValue("Status")
        headerRow.createCell(3).setCellValue("Items")
        headerRow.createCell(4).setCellValue("Total Amount")
        headerRow.createCell(5).setCellValue("Shipping Address")

        // Write each order as a new row
        var rowIndex = 1
        orders.forEach { order ->
            val row = sheet.createRow(rowIndex++)
            row.createCell(0).setCellValue(order.orderId.toString())
            row.createCell(1).setCellValue(order.userId.toString())
            row.createCell(2).setCellValue(
                when (order) {
                    is Order.PendingOrder -> "Pending"
                    is Order.CompletedOrder -> "Completed"
                    is Order.ShippedOrder -> "Shipped"
                    is Order.CancelledOrder -> "Cancelled"
                }
            )
            // Create a summary string of order items, e.g., "prodId1 (x2), prodId2 (x1)"
            val itemsString = order.items.joinToString(separator = ", ") {
                "${it.productId} (x${it.quantity})"
            }
            row.createCell(3).setCellValue(itemsString)

            // For Completed and Shipped orders, show the total amount; otherwise leave blank.
            val totalAmount = when (order) {
                is Order.CompletedOrder -> order.totalAmount
                is Order.ShippedOrder -> order.totalAmount
                else -> null
            }
            row.createCell(4).setCellValue(totalAmount?.toString() ?: "")

            // Only Shipped orders have a shipping address.
            val shippingAddress = when (order) {
                is Order.ShippedOrder -> order.shippingAddress
                else -> ""
            }
            row.createCell(5).setCellValue(shippingAddress)
        }

        // Auto-size columns for better presentation
        for (i in 0..5) {
            sheet.autoSizeColumn(i)
        }

        // Write the workbook to a file. Adjust the path as needed.
        FileOutputStream("orders_report.xlsx").use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()
    }

}