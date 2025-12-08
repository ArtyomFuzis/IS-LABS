import type { TableData } from "../../interface/TableData";
import TableHeadControl from "../TableHeadControl";
import type { Coordinate } from "../../interface/entity/Coordinate";

function CoordinateTable(params : {tableData: TableData<Coordinate>, chosenRow: number, setChosenRow: React.Dispatch<number>, showControls: boolean}) 
{
    return (
        <table cellPadding="8" cellSpacing="0" className="main-table">
        <thead>
            {params.showControls && <tr>
                <th className="main-table-head-labels">ID</th>
                <th className="main-table-head-labels">X</th>
                <th className="main-table-head-labels">Y</th>
            </tr>}
            <tr>
                <TableHeadControl element="id" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="x" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="y" enabled={true} tableData={params.tableData}/>
            </tr>
        </thead>
        <tbody>
            {params.tableData.data.result.map(item => (
                <tr key={item.id} className={`main-table-data-row ${item.id == params.chosenRow ? "main-table-data-row-selected" : ""}`} 
                onClick={()=>params.setChosenRow(item.id)} onDoubleClick={() => params.tableData.setModalOpen("ModifyCoordinate")}>
                    <td className="main-table-elem">{item.id}</td>
                    <td className="main-table-elem">{item.x}</td>
                    <td className="main-table-elem">{item.y}</td>
                </tr>
            ))}
        </tbody>
        </table>
    )
}

export default CoordinateTable