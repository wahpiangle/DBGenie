"use client"

import * as React from "react"
import {
  ColumnDef,
  ColumnFiltersState,
  SortingState,
  VisibilityState,
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table"
import { ArrowUpDown, ChevronDown, MoreHorizontal } from "lucide-react"

import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { useState } from "react"
import API_URL from "@/constants"

export type TableData = {
  rows: any[],
  fields: {
    name: string,
    format: string
  }[]
}

export function DataTableSection(
  { data,
    fields,
    tables,
    onChangeTable,
    selectedTable,
    deleteRow,
  }:
    {
      data: any[],
      fields: { name: string; format: string, dataTypeID: number }[],
      tables: string[],
      onChangeTable: (table: string) => void,
      selectedTable: string,
      deleteRow: (tableName: string, id: string) => void
    }) {
  const [sorting, setSorting] = React.useState<SortingState>([])
  const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>(
    []
  )
  const [columnVisibility, setColumnVisibility] =
    React.useState<VisibilityState>({})
  const [rowSelection, setRowSelection] = React.useState({})
  const [expandedCells, setExpandedCells] = useState<Record<string, boolean>>({});

  const toggleExpand = (cellId: string) => {
    setExpandedCells((prev) => ({
      ...prev,
      [cellId]: !prev[cellId],
    }));
  };

  const columns = fields.map((column) => {
    return {
      accessorKey: column.name,
      header: column.name,
      enableHiding: column.name !== "id",
      cell: (info: { getValue: () => any }) => {
        return column.dataTypeID === 1114 // Date
          ? new Date(info.getValue()).toLocaleDateString()
          : column.dataTypeID === 1700 // Decimal
            ? parseFloat(info.getValue()).toFixed(2)
            : info.getValue();
      },
    }
  })
    .concat({
      accessorKey: "action",
      header: "Actions",
      enableHiding: false,
      cell: (info: any) => {
        return (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="h-8 w-8 p-0">
                <span className="sr-only">Open menu</span>
                <MoreHorizontal />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem
                onClick={() => {
                  deleteRow(
                    selectedTable,
                    info.row.original.id
                  )
                }}
              >
                Delete
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        )
      }
    })

  const table = useReactTable({
    data,
    columns,
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    onColumnVisibilityChange: setColumnVisibility,
    onRowSelectionChange: setRowSelection,
    state: {
      sorting,
      columnFilters,
      columnVisibility,
      rowSelection,
    },
  })

  return (
    <div className="w-full" >
      <div className="flex justify-between py-4">
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" >
              {selectedTable} <ChevronDown />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {tables.map((table) => {
              return (
                <DropdownMenuItem
                  key={table}
                  onClick={() => onChangeTable(table)}
                >
                  {table}
                </DropdownMenuItem>
              )
            })}
          </DropdownMenuContent>
        </DropdownMenu>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" >
              Columns <ChevronDown />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {table
              .getAllColumns()
              .filter((column) => column.getCanHide())
              .map((column) => {
                return (
                  <DropdownMenuCheckboxItem
                    key={column.id}
                    checked={column.getIsVisible()}
                    onCheckedChange={(value) =>
                      column.toggleVisibility(!!value)
                    }
                  >
                    {column.id}
                  </DropdownMenuCheckboxItem>
                )
              })}
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                    </TableHead>
                  )
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow key={row.id} data-state={row.getIsSelected() && "selected"}>
                  {row.getVisibleCells().map((cell) => {
                    const cellId = cell.id;
                    const cellValue = String(cell.getValue() ?? "");
                    const isLong = cellValue.length > 50;
                    const isExpanded = expandedCells[cellId] || false;

                    return (
                      <TableCell key={cellId} className="relative">
                        <div
                          className={`${isLong && !isExpanded ? "truncate max-w-[150px] overflow-hidden text-ellipsis" : "whitespace-normal"
                            }`}
                        >
                          {flexRender(cell.column.columnDef.cell, cell.getContext())}
                        </div>
                        {isLong && (
                          <Button size="sm" onClick={() => toggleExpand(cellId)}>
                            {isExpanded ? "Collapse" : "Expand"}
                          </Button>
                        )}
                      </TableCell>
                    );
                  })}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  className="h-24 text-center"
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex items-center justify-end space-x-2 py-4">
        <div className="space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
          >
            Previous
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}
          >
            Next
          </Button>
        </div>
      </div>
    </div>
  )
}
