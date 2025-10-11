import type { TableData } from "../interfaces/TableData"

function TableHeadControl(params : {element:string, enabled: boolean, tableData: TableData<any>}) 
{
    const sortClick = () => {
        params.tableData.setFilterColumn("")
        params.tableData.setFilterData("")
        if (params.tableData.sortColumn == params.element){
            if (params.tableData.reversedSorting){
                params.tableData.setSortColumn("")
                params.tableData.setReversedSorting(false)
            }
            else{
                params.tableData.setReversedSorting(true)
            }
        }
        else{
            params.tableData.setSortColumn(params.element)
            params.tableData.setReversedSorting(false)
        }
    }

    const filterChanged = (e: any) => {
        params.tableData.setFilterColumn(params.element)
        params.tableData.setFilterData(e.target.value)
        if(e.target.value == ""){
            params.tableData.setFilterColumn("")
        }
    }

    return(
        <th className="main-table-head-controls">
            {
                params.enabled && <div className="main-table-head-controls-container">
                    <span className="main-table-head-controls-arrow">
                        {params.tableData.sortColumn == params.element && (params.tableData.reversedSorting ? "↓" : "↑")}
                    </span>
                    <img src="https://cdn2.iconfinder.com/data/icons/toolbar-icons/512/Funnel-512.png" 
                    className="main-table-head-controls-filter" onClick={sortClick}/>

                    <input type="text" className="main-table-head-controls-input" 
                    value={params.tableData.filterColumn == params.element && params.tableData.filterData || ""} 
                    onChange={filterChanged}
                    ></input>
                </div> 
            }
            
        </th>
    )
}

export default TableHeadControl