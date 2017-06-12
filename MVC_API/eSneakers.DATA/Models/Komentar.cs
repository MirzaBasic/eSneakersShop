using eSneakers.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace eSneakers.DATA.Models
{
    public class Komentar : IEntity
    {
        public int Id { get; set; }
       
        public bool IsDeleted { get; set; }

        public virtual Korisnik Korisnik { get; set; }
        public int KorisnikId { get; set; }
        public virtual Proizvod Proizvod { get; set; }
        public int ProizvodId { get; set; }
        public DateTime DatumKreiranja { get; set; }
        
        public String Sadrzaj { get; set; }
    }
}
