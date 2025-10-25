import type { TableData } from "../../interfaces/TableData";
import type { Location } from "../../interfaces/Entities/Location";
import TableHeadControl from "../TableHeadControl";

function LocationTable(params : {tableData: TableData<Location>, chosenRow: number, setChosenRow: React.Dispatch<number>, showControls:boolean}) 
{
    return (
        <table cellPadding="8" cellSpacing="0" className="main-table">
        <thead>
            <tr>
                <th className="main-table-head-labels">ID</th>
                <th className="main-table-head-labels">Имя</th>
                <th className="main-table-head-labels">X</th>
                <th className="main-table-head-labels">Y</th>
                <th className="main-table-head-labels">Z</th>
            </tr>
            {params.showControls && <tr>
                <TableHeadControl element="id" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="name" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="x" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="y" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="z" enabled={true} tableData={params.tableData}/>
            </tr>}
        </thead>
        <tbody>
            {params.tableData.data.result.map(item => (
                <tr key={item.id} className={`main-table-data-row ${item.id == params.chosenRow ? "main-table-data-row-selected" : ""}`} 
                onClick={()=>params.setChosenRow(item.id)} onDoubleClick={() => params.tableData.setModalOpen("ModifyLocation")}>
                    <td className="main-table-elem">{item.id}</td>
                    <td className="main-table-elem">{item.name}</td>
                    <td className="main-table-elem">{item.x}</td>
                    <td className="main-table-elem">{item.y}</td>
                    <td className="main-table-elem">{item.z}</td>
                </tr>
            ))}
        </tbody>
        </table>
    )
}

export default LocationTable